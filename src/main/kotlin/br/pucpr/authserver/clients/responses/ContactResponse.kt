package br.pucpr.authserver.clients.responses

import br.pucpr.authserver.clients.ClientContact

data class ContactResponse(
    val typeContact: String,
    val valueContact: String,
    val labelContact: String
) {
    constructor(contact: ClientContact) : this(
        typeContact = contact.typeContact,
        valueContact = contact.valueContact,
        labelContact = contact.labelContact
    )
}