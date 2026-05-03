package br.pucpr.authserver.sales.requests

import br.pucpr.authserver.exceptions.BadRequestException
import br.pucpr.authserver.parts.PartService
import br.pucpr.authserver.sales.Sale
import br.pucpr.authserver.sales.SaleService
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Positive
import org.springframework.beans.factory.annotation.Autowired
import java.math.BigDecimal
import java.time.ZoneId
import java.time.ZonedDateTime

data class CreateSaleRequest (

    @field:NotNull("Id of the Part is required")
    @field:Positive(message = "Value must be greater than zero")
    var idPartSale: Long? = null,

    @field:NotNull(message = "Quantity of the Part is required")
    @field:Positive(message = "Value must be greater than zero")
    var qtnPartSale: Long? = null,

    @field:NotNull(message = "Id of the Client is required")
    @field:Positive(message = "Value must be greater than zero")
    var idClientSale: Long? = null,
) {

    fun toSale(userCreateSale: Long): Sale = Sale (
        idUserSale = userCreateSale,
        idClientSale = idClientSale!!,
        idPartSale = idPartSale!!,
        qtnPartSale = qtnPartSale!!,
        finalValueSale = BigDecimal.ZERO,
        dateTimeSale = ZonedDateTime.now(ZoneId.of("America/Sao_Paulo")),
    )

}