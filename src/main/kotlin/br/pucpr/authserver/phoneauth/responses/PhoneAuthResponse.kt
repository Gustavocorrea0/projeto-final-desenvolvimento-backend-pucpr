package br.pucpr.authserver.phoneauth.responses

import br.pucpr.authserver.phoneauth.PhoneAuth
import java.time.ZonedDateTime
import java.util.UUID

data class PhoneAuthResponse(
    val idPhoneUser: Long?,
    val uuidUser: UUID,
    val phoneNumber: String,
    val nameUser: String,
    val dateTimeCreate: ZonedDateTime,
    val dateTimeUpdate: ZonedDateTime
) {
    constructor(phoneAuth: PhoneAuth): this(
        idPhoneUser = phoneAuth.idPhoneUser,
        uuidUser = phoneAuth.uuidUser,
        phoneNumber = phoneAuth.phoneNumber,
        nameUser = phoneAuth.nameUser,
        dateTimeCreate = phoneAuth.dateTimeCreate,
        dateTimeUpdate = phoneAuth.dateTimeUpdate
    )
}
