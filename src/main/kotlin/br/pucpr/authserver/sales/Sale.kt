package br.pucpr.authserver.sales

import jakarta.persistence.CascadeType
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.GeneratedValue
import jakarta.persistence.Id
import jakarta.persistence.OneToMany
import jakarta.persistence.Table
import java.math.BigDecimal
import java.time.ZonedDateTime

@Entity
@Table(name = "SalesTable")
class Sale (

    @Id
    @GeneratedValue
    var idSale: Long? = null,

    @Column(nullable = false, updatable = false)
    var idUserSale: Long? = 0,

    @Column(nullable = false)
    var idClientSale: Long? = 0,

    @OneToMany(
        mappedBy = "sale",
        cascade = [(CascadeType.ALL)],
        orphanRemoval = true,
        fetch = FetchType.EAGER
    )
    var salesParts: MutableList<SalePart> = mutableListOf(),

    @Column(nullable = false)
    var finalValueSale: BigDecimal? = BigDecimal.ZERO,

    @Column(nullable = false, updatable = false)
    var dateTimeSale: ZonedDateTime,

) {
}