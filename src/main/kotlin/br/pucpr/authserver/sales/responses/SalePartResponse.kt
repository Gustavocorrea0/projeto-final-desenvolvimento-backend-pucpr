package br.pucpr.authserver.sales.responses

import br.pucpr.authserver.sales.SalePart

data class SalePartResponse(
    var idPartSale: Long,
    val qtnPartSale: Long,
) {
    constructor(salePart: SalePart) : this(
        idPartSale = salePart.idPartSale!!,
        qtnPartSale = salePart.qtnPartSale!!
    )
}