package br.pucpr.authserver.clients.responses

import br.pucpr.authserver.clients.Client
import java.time.ZonedDateTime

// ─────────────────────────────────────────────────────────────
// ClientSummaryResponse — Resposta resumida (listagem geral)
// ─────────────────────────────────────────────────────────────
// Usado no endpoint GET /clients (lista todos).
// Retorna APENAS id e nome, evitando expor dados desnecessários
// e reduzindo o tamanho da resposta em listagens grandes.
data class ClientSummaryResponse(
    val idClient: Long,
    val nameClient: String
) {
    // Secondary constructor: recebe uma entidade Client e extrai apenas os campos necessários.
    // Isso desacopla o formato da resposta da estrutura interna da entidade.
    constructor(client: Client) : this(
        idClient   = client.idClient!!,
        nameClient = client.nameClient
    )
}

// ─────────────────────────────────────────────────────────────
// ClientResponse — Resposta completa (busca por ID)
// ─────────────────────────────────────────────────────────────
// Usado no endpoint GET /clients/{id} (busca único).
// Retorna TODOS os campos do cliente, inclusive metadados
// como datas, usuário criador e status ativo.
//
// Nota: password e outros dados sensíveis do usuário NÃO
// são expostos — apenas o ID de quem criou/atualizou.
data class ClientResponse(
    val idClient: Long,
    val nameClient: String,
    val contactClient: String,
    val dateTimeCreateClient: ZonedDateTime,
    val userCreateClient: Long,
    val userUpdateClient: Long,
    val activeClient: Boolean
) {
    // Converte a entidade completa para o DTO de resposta detalhada.
    constructor(client: Client) : this(
        idClient             = client.idClient!!,
        nameClient           = client.nameClient,
        contactClient        = client.contactClient,
        dateTimeCreateClient = client.dateTimeCreateClient,
        userCreateClient     = client.userCreateClient,
        userUpdateClient     = client.userUpdateClient,
        activeClient         = client.activeClient
    )
}