package br.pucpr.authserver.parts.requests

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Positive
import jakarta.validation.constraints.Size
import java.math.BigDecimal

data class UpdatePartRequest (

    @field:NotBlank("Name of the part is required")
    @field:Size(min = 1, max = 50, message = "Name must be between 1 and 50")
    val namePart: String?,

    @field:NotBlank("Brand of the part is required")
    @field:Size(min = 1, max = 50, message = "Brand must be between 1 and 50")
    val brandPart: String?,

    @field:NotBlank("Description of the part is required")
    @field:Size(min = 1, max = 500, message = "Description must be between 1 and 500")
    val descriptionPart: String?,

    @field:NotNull(message = "Quantity of the part is required")
    @field:Positive(message = "Quantity must be greater than zero")
    val quantityPart: Long?,

    @field:NotNull(message = "Value of the part is required")
    @field:Positive(message = "Value must be greater than zero")
    val valuePart: BigDecimal?,

) {
}