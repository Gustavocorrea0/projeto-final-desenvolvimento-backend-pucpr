package br.pucpr.authserver.phoneauth

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface PhoneAuthRepository: JpaRepository<PhoneAuth, Long> {
    fun findByPhoneNumber(phoneNumber: String): Boolean?
    fun findByUUIDOrNull(uuid: java.util.UUID): Boolean?
}