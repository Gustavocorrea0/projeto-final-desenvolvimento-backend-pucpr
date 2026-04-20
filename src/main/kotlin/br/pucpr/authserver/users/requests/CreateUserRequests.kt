package br.pucpr.authserver.users.requests

import br.pucpr.authserver.users.User
import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Pattern
import jakarta.validation.constraints.Size

// DTO - Data Transfer Object
data class CreateUserRequests(
    @NotBlank
    val name: String?,

    @NotBlank
    @Email
    val email: String?,

    @NotBlank
    @Pattern(regexp = "[a-zA-Z0-9_]{4,}")
    val password: String?
) {
    fun toUser(): User = User(
        name = name!!,
        email = email ?: "",
        password = password ?: ""
    )
}