package br.pucpr.authserver.phoneauth

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
interface PhoneAuthRepository: JpaRepository<PhoneAuth, Long> {
    fun findByPhoneNumber(phoneNumber: String): PhoneAuth?
    fun findByUuidUser(uuid: UUID): PhoneAuth?
}