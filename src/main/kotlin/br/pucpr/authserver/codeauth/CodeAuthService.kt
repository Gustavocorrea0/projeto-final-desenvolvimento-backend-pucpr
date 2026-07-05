package br.pucpr.authserver.codeauth

import br.pucpr.authserver.exceptions.NotFoundException
import br.pucpr.authserver.exceptions.UnauthorizedException
import br.pucpr.authserver.parts.PartService
import br.pucpr.authserver.phoneauth.PhoneAuthService
import org.slf4j.LoggerFactory
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.ZonedDateTime

@Service
class CodeAuthService (
    private val codeAuthRepository: CodeAuthRepository
) {

    companion object {
        private val log = LoggerFactory.getLogger(PhoneAuthService::class.java)
        const val  MAX_ATTEMPTS = 5
        const val  EXPIRATION_MINUTES = 5L
    }

    fun saveCodeAuth(phoneNumber: String, code: String): CodeAuth {
        codeAuthRepository.findByPhoneNumberUser(phoneNumber) ?.also { codeAuthRepository.delete(it) }

        val codeAuth = CodeAuth(
            phoneNumberUser = phoneNumber,
            authCode = code,
            // O código expira 5 minutos a partir de agora
            dateTimeExpire = ZonedDateTime.now().plusMinutes(EXPIRATION_MINUTES),
            attempts = 0
        )

        return codeAuthRepository.save(codeAuth)
            .also { log.debug("CodeAuth save for phone {}", phoneNumber) }

    }

    fun validateCodeAuth(phoneNumber: String, code: String): Boolean {
        val record = codeAuthRepository.findByPhoneNumberUser(phoneNumber) ?:
            throw NotFoundException("Nenhum código encontrado para este telefone")

        // Verifica se o código já expirou
        if (ZonedDateTime.now().isAfter(record.dateTimeExpire)) {
            codeAuthRepository.delete(record)
            log.warn("Código expirado para o telefone: {}", phoneNumber)
            throw UnauthorizedException("Código expirado. Solicite um novo código.")
        }

        // Verifica se o limite de tentativas foi atingido
        if (record.attempts >= MAX_ATTEMPTS) {
            codeAuthRepository.delete(record)
            log.warn("Limite de tentativas atingido para o telefone: {}", phoneNumber)
            throw UnauthorizedException("Limite de tentativas atingido. Solicite um novo código.")
        }

        // Código incorreto: incrementa tentativas e salva
        if (record.authCode != code) {
            record.attempts++
            codeAuthRepository.save(record)
            log.warn("Código incorreto para telefone: {} | tentativas: {}", phoneNumber, record.attempts)
            throw NotFoundException("Código inválido.")
        }

        // Código correto: remove o registro (confirmação concluída) e retorna sucesso
        codeAuthRepository.delete(record)
        log.info("Código validado com sucesso para o telefone: {}", phoneNumber)

        return true

    }

    // Remove manualmente o código associado a um telefone (ex.: após atualização de UUID)
    fun deleteCodeByPhone(phoneNumber: String) {
        codeAuthRepository.findByPhoneNumberUser(phoneNumber)
            ?.also { codeAuthRepository.delete(it) }
            ?: log.warn("Tentativa de deletar código inexistente para o telefone: {}", phoneNumber)
    }

    // Scheduler que roda a cada minuto e apaga automaticamente os códigos expirados do banco
    @Scheduled(fixedDelay = 60_000)
    @Transactional
    fun purgeExpiredCodes() {
        val now = ZonedDateTime.now()
        codeAuthRepository.deleteAllExpired(now)
        log.info("Limpeza de códigos expirados executada às: {}", now)
    }

}