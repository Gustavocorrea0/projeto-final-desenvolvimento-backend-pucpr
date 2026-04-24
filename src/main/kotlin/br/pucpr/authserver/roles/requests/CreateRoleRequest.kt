package br.pucpr.authserver.roles.requests

import br.pucpr.authserver.roles.Role
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Size

data class CreateRoleRequest(
    @NotBlank
    @NotNull
    var name: String,

    @NotBlank
    @NotNull
    @Size(min = 0, max = 100)
    var description: String
) {

    fun toRole(): Role = Role(
        name = name,
        description = description
    )

}