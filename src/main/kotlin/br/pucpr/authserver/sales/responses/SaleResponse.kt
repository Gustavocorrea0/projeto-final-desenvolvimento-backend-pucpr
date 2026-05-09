package br.pucpr.authserver.sales.responses

import br.pucpr.authserver.sales.Sale
import java.math.BigDecimal
import java.time.ZonedDateTime

// GET ALL
data class SaleSummaryResponse (
    val idSale: Long? = null,
    val idUserSale: Long? = null,
    val idClientSale: Long? = null,
    val finalValueSale: BigDecimal? = BigDecimal.ZERO,
) {
    constructor(sale: Sale) : this(
        idSale = sale.idSale,
        idUserSale = sale.idUserSale,
        idClientSale = sale.idClientSale,
        finalValueSale = sale.finalValueSale
    )
}

// GET ONE
data class SaleResponse (
    var idSale: Long? = null,
    var idUserSale: Long? = 0,
    var idClientSale: Long? = 0,
    var salesParts: List<SalePartResponse>,
    var finalValueSale: BigDecimal? = BigDecimal.ZERO,
    var dateTimeSale: ZonedDateTime
) {
    constructor(sale: Sale) : this(
        idSale = sale.idSale,
        idUserSale = sale.idUserSale,
        idClientSale = sale.idClientSale,
        salesParts = sale.salesParts.map { SalePartResponse(it) },
        finalValueSale = sale.finalValueSale,
        dateTimeSale = sale.dateTimeSale
    )
}