package br.pucpr.authserver.parts.requests

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Positive
import jakarta.validation.constraints.Size

data class AddPartsToExisting(
    @field:NotNull(message = "Quantity of the part is required")
    @field:Positive(message = "Quantity must be greater than zero")
    val quantityPart: Long?,
) {
}