package br.pucpr.authserver.phoneauth.requests

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import java.util.UUID

data class PhoneAuthRequest(
    @field:NotBlank(message = "PhoneNumber cannot be blank")
    val phoneNumber: String,

    @field:NotNull(message = "UUID cannot be blank")
    val uuidUser: UUID
)

data class ConfirmCodeRequest(
    @field:NotBlank(message = "Phone cannot be blank")
    val phoneNumber: String,

    @field:NotBlank(message = "SMS Code cannot be blank")
    val code: String,

    @field:NotNull(message = "UUID cannot be blank")
    val uuidUser: UUID,

    @field:NotBlank(message = "Name cannot be blank")
    val nameUser: String,
)
