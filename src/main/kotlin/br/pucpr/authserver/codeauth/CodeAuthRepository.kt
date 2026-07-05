package br.pucpr.authserver.codeauth

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import java.time.ZonedDateTime

@Repository
interface CodeAuthRepository: JpaRepository<CodeAuth, Long> {
    // Busca o registro pelo telefone — usado para validar o código e controlar tentativas
    fun findByPhoneNumberUser(phoneNumberUser: String): CodeAuth?

    @Modifying
    @Query("DELETE FROM CodeAuth c WHERE c.dateTimeExpire < :now")
    fun deleteAllExpired(now: ZonedDateTime)
}