package br.pucpr.authserver.codeauth

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.time.ZonedDateTime

@Entity
@Table(name = "code_auth")
class CodeAuth(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var idAuthCode: Long? = null,

    @Column(nullable = false)
    var phoneNumberUser: String? = null,

    @Column(nullable = false)
    var authCode: String? = null,

    @Column(nullable = false)
    var dateTimeExpire: ZonedDateTime? = null,

)