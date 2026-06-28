package br.pucpr.authserver.codeauth

import br.pucpr.authserver.exceptions.NotFoundException
import br.pucpr.authserver.parts.PartService
import br.pucpr.authserver.phoneauth.PhoneAuthService
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
class CodeAuthService (
    private val codeAuthRepository: CodeAuthRepository
) {

    companion object {
        private val log = LoggerFactory.getLogger(PhoneAuthService::class.java)
    }

    fun saveCodeAuth(codeAuth: CodeAuth): CodeAuth {
        val saveCode = codeAuthRepository.save(codeAuth)
        log.info("saved code auth with id = {}", saveCode)
        return saveCode
    }

    fun searchCodeAuth(phoneNumber: String, code: String): Boolean? {
        val codeIsFound: Boolean = codeAuthRepository.findByPhoneNumberAndAuthCode(
            phoneNumber,
            code
        )?: throw NotFoundException("Code is Invalid")
        return codeIsFound
    }

    fun deleteCodeAuth(phoneNumber: String, code: String) {
        val codeIsFound = codeAuthRepository.findByPhoneNumberAndAuthCodeForDelete(
            phoneNumber,
            code
        )?: throw NotFoundException("Code is Invalid")
        codeAuthRepository.delete(codeIsFound)
    }

}