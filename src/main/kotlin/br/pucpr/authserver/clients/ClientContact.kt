package br.pucpr.authserver.clients

import jakarta.annotation.Generated
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table

@Entity
@Table(name = "ClientContactTable")
class ClientContact(
    @Id
    @GeneratedValue
    var id: Long? = null,

    @Column(nullable = false)
    var typeContact: String,

    @Column(nullable = false)
    var valueContact: String,

    @Column(nullable = false)
    var labelContact: String,

    @ManyToOne
    @JoinColumn(name = "idClient", nullable = false)
    var client: Client
) {

}