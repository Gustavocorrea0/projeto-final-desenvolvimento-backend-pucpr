package br.pucpr.authserver.phoneauth

import br.pucpr.authserver.codeauth.CodeAuthService
import io.github.cdimascio.dotenv.dotenv
import org.springframework.stereotype.Service
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.transaction.annotation.Transactional
import java.util.UUID
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider
import software.amazon.awssdk.services.sns.SnsClient
import software.amazon.awssdk.regions.Region
import software.amazon.awssdk.services.sns.model.MessageAttributeValue
import software.amazon.awssdk.services.sns.model.PublishRequest
import java.time.ZonedDateTime
import kotlin.random.Random

@Service
class PhoneAuthService (
    private val phoneAuthRepository: PhoneAuthRepository,
    private val codeAuthService: CodeAuthService
) {

    companion object {
        private val log = LoggerFactory.getLogger(PhoneAuthService::class.java)
    }

    sealed class AuthStep1Result {
        // telefone e uuid existem (autenticado)
        data class Authenticated(val phoneAuth: PhoneAuth) : AuthStep1Result()
        // telefone encontrado ou nao encontrado (enviar sms)
        object CodeSent : AuthStep1Result()
    }

    fun checkPhoneAndUUID(phoneNumber: String, uuidUser: UUID): AuthStep1Result {
        val byPhone = phoneAuthRepository.findByPhoneNumber(phoneNumber)

        return when {
            // Caso 1 — Telefone e UUID coincidem: autentica diretamente
            byPhone != null && byPhone.uuidUser == uuidUser -> {
                log.info("Caso 1 — Autenticação direta. Telefone: {}", phoneNumber)
                AuthStep1Result.Authenticated(byPhone)
            }

            // Caso 2 — Telefone existe com UUID diferente: envia SMS para atualizar UUID
            byPhone != null && byPhone.uuidUser != uuidUser -> {
                log.info("Caso 2 — UUID divergente. Enviando novo código. Telefone: {}", phoneNumber)
                sendSms(phoneNumber)
                AuthStep1Result.CodeSent
            }

            // Caso 3 — Telefone não existe: envia SMS para novo cadastro
            else -> {
                log.info("Caso 3 — Novo usuário. Enviando código. Telefone: {}", phoneNumber)
                sendSms(phoneNumber)
                AuthStep1Result.CodeSent
            }
        }
    }

    // Resultado do passo 2: informa ao controller se foi cadastro novo ou atualização
    sealed class AuthStep2Result {
        data class Created(val phoneAuth: PhoneAuth) : AuthStep2Result()   // Caso 3
        data class Updated(val phoneAuth: PhoneAuth) : AuthStep2Result()   // Caso 2
    }

    @Transactional
    fun confirmCode(phoneNumber: String, code: String, uuidUser: UUID, nameUser: String): AuthStep2Result {
        codeAuthService.validateCodeAuth(phoneNumber, code)

        val existing = phoneAuthRepository.findByPhoneNumber(phoneNumber)

        return if (existing != null) {
            // Caso 2 — Telefone já cadastrado: atualiza o UUID
            existing.uuidUser = uuidUser
            existing.dateTimeUpdate = ZonedDateTime.now()
            phoneAuthRepository.save(existing)
                .also { log.info("Caso 2 — UUID atualizado para o telefone: {}", phoneNumber) }
                .let { AuthStep2Result.Updated(it) }
        } else {
            // Caso 3 — Telefone novo: cria o registro
            val newUser = PhoneAuth(
                uuidUser = uuidUser,
                phoneNumber = phoneNumber,
                nameUser = nameUser,
                dateTimeCreate = ZonedDateTime.now(),
                dateTimeUpdate = ZonedDateTime.now()
            )
            phoneAuthRepository.save(newUser)
                .also { log.info("Caso 3 — Novo usuário cadastrado. Telefone: {}", phoneNumber) }
                .let { AuthStep2Result.Created(it) }
        }
    }

    private fun sendSms(phoneNumber: String) {

        val code = Random.nextInt(100000, 999999).toString()
        val dotenv = dotenv()

        // As credenciais AWS são lidas automaticamente das variáveis de ambiente:
        // AWS_ACCESS_KEY_ID e AWS_SECRET_ACCESS_KEY (definidas no .env / sistema)
        val sns = SnsClient.builder()
            .region(Region.of(dotenv["AWS_REGION"]))
            .credentialsProvider (
                StaticCredentialsProvider.create(
                    AwsBasicCredentials.create(
                        dotenv["AWS_ACCESS_KEY_ID"],
                        dotenv["AWS_SECRET_ACCESS_KEY"]
                    )
                )
            )
            .build()

        log.info("Code is: {}", code)

        val request = PublishRequest.builder()
            .phoneNumber(phoneNumber)
            .message("Seu código de autenticação é: $code. Válido por 5 minutos.")
            .messageAttributes(
                mapOf(
                    // Garante entrega prioritária (não promocional)
                    "AWS.SNS.SMS.SMSType" to MessageAttributeValue.builder()
                        .dataType("String")
                        .stringValue("Transactional")
                        .build()
                )
            )
            .build()

        sns.use { client ->
            val response = client.publish(request)
            log.info("SMS enviado para: {} | MessageId: {}", phoneNumber, response.messageId())
        }

        // Persiste o código no banco para validação posterior
        codeAuthService.saveCodeAuth(phoneNumber, code)
    }

}