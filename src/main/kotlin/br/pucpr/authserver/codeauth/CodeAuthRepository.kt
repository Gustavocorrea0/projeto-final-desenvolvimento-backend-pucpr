package br.pucpr.authserver.codeauth

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface CodeAuthRepository: JpaRepository<CodeAuth, Long> {
    fun findByPhoneNumberAndAuthCode(phoneNumberUser: String, authCode: String): Boolean?
    fun findByPhoneNumberAndAuthCodeForDelete(phoneNumberUser: String, authCode: String): CodeAuth?
}