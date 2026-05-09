package br.pucpr.authserver.sales.requests

import br.pucpr.authserver.sales.Sale
import br.pucpr.authserver.sales.SalePart
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Positive

data class SalePartRequest(
    @field:NotNull("Id of the Part is required")
    @field:Positive(message = "Value must be greater than zero")
    var idPartSale: Long? = null,

    @field:NotNull(message = "Quantity of the Part is required")
    @field:Positive(message = "Value must be greater than zero")
    var qtnPartSale: Long? = null,
) {
    fun toSalePart(sale: Sale): SalePart = SalePart(
        idPartSale = idPartSale!!,
        qtnPartSale = qtnPartSale!!,
        sale = sale
    )
}