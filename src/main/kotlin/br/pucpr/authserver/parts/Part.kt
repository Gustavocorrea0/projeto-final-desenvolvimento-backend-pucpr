package br.pucpr.authserver.parts

import jakarta.persistence.*
import java.math.BigDecimal
import java.time.ZonedDateTime

@Entity
@Table(name = "PartsTable")
class Part (

    // Outside JSON
    @Id
    @GeneratedValue
    var idPart: Long? = null,

    // Side JSON
    @Column(nullable = false, length = 50)
    var namePart: String? = null,

    @Column(nullable = false, length = 50)
    var brandPart: String? = null,

    @Column(nullable = false, length = 500)
    var descriptionPart: String? = null,

    @Column(nullable = false)
    var quantityPart: Long? = 0,

    @Column(nullable = false)
    var valuePart: BigDecimal? = BigDecimal.ZERO,

    // Outside JSON
    @Column(nullable = false, updatable = false)
    var dateTimeCreatePart: ZonedDateTime,

    @Column(nullable = false, updatable = false)
    var dateTimeUpdatePart: ZonedDateTime,

    @Column(nullable = false)
    var userCreatePart: Long,

    @Column(nullable = false)
    var userUpdatePart: Long

)