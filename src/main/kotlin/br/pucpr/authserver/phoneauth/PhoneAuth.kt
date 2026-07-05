package br.pucpr.authserver.phoneauth

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.time.ZonedDateTime
import java.util.UUID

@Entity
@Table(name = "phone_auth")
class PhoneAuth(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var idPhoneUser: Long? = null,

    @Column(nullable = false)
    var uuidUser: UUID,

    @Column(nullable = false, updatable = false)
    var phoneNumber: String,

    @Column(nullable = false)
    var nameUser: String,

    @Column(nullable = false, updatable = false)
    var dateTimeCreate: ZonedDateTime,

    @Column(nullable = false)
    var dateTimeUpdate: ZonedDateTime

)