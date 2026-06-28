package br.pucpr.authserver.phoneauth

import br.pucpr.authserver.codeauth.CodeAuthService
import br.pucpr.authserver.exceptions.NotFoundException
import org.aspectj.runtime.reflect.Factory
import org.springframework.stereotype.Service
import org.slf4j.LoggerFactory
import java.util.UUID
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider
import software.amazon.awssdk.services.sns.SnsClient
import software.amazon.awssdk.regions.Region
import software.amazon.awssdk.services.sns.model.MessageAttributeValue
import software.amazon.awssdk.services.sns.model.PublishRequest
import kotlin.random.Random

@Service
class PhoneAuthService (
    private val phoneAuthRepository: PhoneAuthRepository,
    private val codeAuthService: CodeAuthService
) {

    companion object {
        private val log = LoggerFactory.getLogger(PhoneAuthService::class.java)
    }

    // funcao que cadastra novo UUID e telefone apos validacao do codigo ENVIADO para TELEFONE COM AWS SNS
    // funcao que substitui UUID e envio codigo para TELEFONE COM AWS SNS

    /*
    * 1 - Caso o telefone e uuid existam para um usuário ativo, faça o login e retorne o usuário.
    *   > criar funcao que valida UUID e TELEFONE
    *       > caso 1 - existe: Faz Login
    *       > caso 2 - telefone existe com outro UUID: enviar novo codigo de validacao e alterar ID em caso de sucesso\
    *       > caso 3 - telefone nao existe: criar um novo cadastro com autenticao (regra 2)
    */

    /*
    * 2 - Caso o telefone não exista, ou exista com outro uuid, envie um SMS de confirmação e retorne o código http 202
    *
    *   > body confirmacao: telefone, id e codigo
    *   caso 1 - codigo incorreto: retornar erro 404
    *   caso 2 - codigo correto cadastra usuario
    *
    */

    fun searchPhoneAndUUID(phoneUser: String, uuidUser: UUID): String? {
        val phoneIsFound: Boolean = phoneAuthRepository.findByPhoneNumber(phoneUser) ?: throw NotFoundException("Phone not found")
        val uuidIsFound: Boolean = phoneAuthRepository.findByUUIDOrNull(uuidUser) ?: throw NotFoundException("Phone not found")
        if (phoneIsFound) {
            return if (uuidIsFound) { "phone and uuid is found" }
            else { "phone is found" }
        }
        return "phone and uuid is not found"
    }

    // 1 - enviar codigo para novo cadastro
    fun sendNewCodeForNewUser(phoneNumber: String) {

        val code: String = Random.nextInt(100000, 999999).toString()
        val sns = SnsClient.builder()
            .region(Region.US_EAST_1)
            .build()

        val request = PublishRequest.builder()
            .phoneNumber(phoneNumber)
            .message("Olá, Bem-Vindo Ao AuthApp, este é seu Codigo de Autencatição: $code")
            .messageAttributes(
                mapOf(
                    "AWS.SNS.SMS.SMSType" to MessageAttributeValue.builder()
                        .dataType("String")
                        .stringValue("Transactional")
                        .build()
                )
            )
            .build()

        val response = sns.publish(request)
        sns.close()

        log.info("New code: $response")
        codeAuthService.saveCodeAuth(phoneUser, code)

    }

    // 2 - enviar codigo para cadastro existente
    fun sendNewCodeForExistingUser(phoneNumber: String) : Boolean? { return false }

    // 3 - comparar codigos
    fun validateAuthCode(code: String) : Boolean? { return false }

    // 4 - salvar novo uuid e salvar novo telefone
    fun savePhoneAndUUID(code: String) : Boolean? { return false }

    // 5 - atualizar uuid de um telefone
    fun updatePhoneAndUUID(code: String) : Boolean? { return false }

}