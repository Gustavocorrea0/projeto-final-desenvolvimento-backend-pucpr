package br.pucpr.authserver.parts.requests

import br.pucpr.authserver.parts.Part
import jakarta.validation.constraints.*
import jakarta.validation.constraints.Size
import java.math.BigDecimal
import java.time.ZoneId
import java.time.ZonedDateTime

data class CreatePartRequest(

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

    fun toPart(userCreatePart: Long): Part = Part (
        namePart = namePart!!,
        brandPart = brandPart!!,
        descriptionPart = descriptionPart!!,
        quantityPart = quantityPart!!,
        valuePart = valuePart!!,
        dateTimeCreatePart = ZonedDateTime.now(ZoneId.of("America/Sao_Paulo")),
        dateTimeUpdatePart = ZonedDateTime.now(ZoneId.of("America/Sao_Paulo")),
        userCreatePart = userCreatePart,
        userUpdatePart = userCreatePart
    )

}