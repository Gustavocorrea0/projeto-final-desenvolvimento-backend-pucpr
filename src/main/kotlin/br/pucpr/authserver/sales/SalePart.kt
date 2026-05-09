package br.pucpr.authserver.sales

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table
import javax.annotation.processing.Generated

@Entity
@Table(name = "SalePart")
class SalePart(
    @Id
    @GeneratedValue
    var id: Long? = null,

    @Column(nullable = false)
    var idPartSale: Long? = 0,

    @Column(nullable = false)
    var qtnPartSale: Long? = 0,

    @ManyToOne
    @JoinColumn(name = "idSale", nullable = false)
    var sale: Sale,
) {

}