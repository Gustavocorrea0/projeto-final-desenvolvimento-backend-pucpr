package br.pucpr.authserver.sales.requests

import br.pucpr.authserver.clients.requests.ContactRequest
import br.pucpr.authserver.exceptions.BadRequestException
import br.pucpr.authserver.parts.PartService
import br.pucpr.authserver.sales.Sale
import br.pucpr.authserver.sales.SalePart
import br.pucpr.authserver.sales.SaleService
import jakarta.validation.Valid
import jakarta.validation.constraints.NotEmpty
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Positive
import org.springframework.beans.factory.annotation.Autowired
import java.math.BigDecimal
import java.time.ZoneId
import java.time.ZonedDateTime

data class CreateSaleRequest (

    @field:NotEmpty(message = "Parts is required")
    @field:Valid
    val saleParts: List<SalePartRequest>?,

    @field:NotNull(message = "Id of the Client is required")
    @field:Positive(message = "Value must be greater than zero")
    var idClientSale: Long? = null,
) {

    fun toSale(userCreateSale: Long): Sale {
        val now = ZonedDateTime.now(ZoneId.of("America/Sao_Paulo"))

        val newSale = Sale(
            idUserSale = userCreateSale,
            idClientSale = idClientSale!!,
            finalValueSale = BigDecimal.ZERO,
            dateTimeSale = now
        )

        val salePartsEntities = saleParts!!.map { it.toSalePart(newSale) }
        newSale.salesParts.addAll(salePartsEntities)
        return newSale
    }


    /*fun toSale(userCreateSale: Long): Sale = Sale (
        idUserSale = userCreateSale,
        idClientSale = idClientSale!!,
        idPartSale = idPartSale!!,
        qtnPartSale = qtnPartSale!!,
        finalValueSale = BigDecimal.ZERO,
        dateTimeSale =
    )*/

}