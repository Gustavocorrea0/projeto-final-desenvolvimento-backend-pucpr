package br.pucpr.authserver.clients.requests

import br.pucpr.authserver.clients.Client
import br.pucpr.authserver.clients.ClientContact
import jakarta.validation.constraints.NotBlank

data class ContactRequest(
    @field:NotBlank(message = "The contact type is required")
    val typeContact: String?,

    @field:NotBlank(message = "The contact value is required")
    val valueContact: String?,

    @field:NotBlank(message = "The contact label is required")
    val labelContact: String?
) {
    fun toContact(client: Client): ClientContact = ClientContact(
        typeContact   = typeContact!!,
        valueContact  = valueContact!!,
        labelContact  = labelContact!!,
        client = client   // associa o contato ao client que acabou de ser criado
    )
}