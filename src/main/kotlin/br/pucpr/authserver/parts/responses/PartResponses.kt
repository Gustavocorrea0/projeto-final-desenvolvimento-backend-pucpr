package br.pucpr.authserver.parts.responses

import br.pucpr.authserver.parts.Part
import java.math.BigDecimal
import java.time.ZonedDateTime

// GET ALL
data class PartSummaryResponse(
    var idPart: Long? = null,
    var namePart: String? = null,
    var brandPart: String? = null,
    var quantityPart: Long? = null,
    var valuePart: BigDecimal? = null,
) {
    constructor(part: Part) : this(
        idPart = part.idPart,
        namePart = part.namePart,
        brandPart = part.brandPart,
        quantityPart = part.quantityPart,
        valuePart = part.valuePart
    )
}

// GET ONE
data class PartResponse (
    var idPart: Long? = null,
    var namePart: String? = null,
    var brandPart: String? = null,
    var descriptionPart: String? = null,
    var quantityPart: Long? = null,
    var valuePart: BigDecimal? = null,
    var dateTimeCreatePart: ZonedDateTime,
    var dateTimeUpdatePart: ZonedDateTime,
    var userCreatePart: Long,
    var userUpdatePart: Long
) {
    constructor(part: Part): this(
        idPart = part.idPart!!,
        namePart = part.namePart,
        brandPart = part.brandPart,
        descriptionPart = part.descriptionPart,
        quantityPart = part.quantityPart,
        valuePart = part.valuePart,
        dateTimeCreatePart = part.dateTimeCreatePart,
        dateTimeUpdatePart = part.dateTimeUpdatePart,
        userCreatePart = part.userCreatePart,
        userUpdatePart = part.userUpdatePart
    )
}

// Return value
data class PartResponseSale (
    var idPart: Long? = null,
    var valuePart: BigDecimal? = null,
) {
    constructor(part: Part): this(
        idPart = part.idPart,
        valuePart = part.valuePart
    )
}