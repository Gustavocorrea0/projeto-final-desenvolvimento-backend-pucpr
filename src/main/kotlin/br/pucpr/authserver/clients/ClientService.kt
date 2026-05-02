package br.pucpr.authserver.clients

import br.pucpr.authserver.exceptions.NotFoundException
import org.slf4j.LoggerFactory
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service

// @Service: marca esta classe como componente de serviço do Spring.
// O Spring a instancia automaticamente e injeta onde for necessário (ex: no Controller).
//
// O Service é a camada de REGRAS DE NEGÓCIO: ele decide o que pode ou não acontecer.
// O Controller apenas recebe a requisição e delega ao Service.
// O Repository apenas acessa o banco — sem lógica.
@Service
class ClientService(
    // Injeção de dependência via construtor (padrão recomendado no Spring com Kotlin).
    // O Spring injeta automaticamente o bean ClientRepository registrado no contexto.
    private val repository: ClientRepository
) {

    // ─────────────────────────────────────────────────────────────
    // INSERT — Criar um novo cliente
    // ─────────────────────────────────────────────────────────────
    // Recebe uma entidade Client já montada pelo DTO (via toClient(userId)).
    // Persiste no banco e loga o ID gerado.
    // Retorna o Client salvo (com idClient preenchido pelo banco).
    fun insert(client: Client): Client {
        val saved = repository.save(client)
        log.info("Cliente criado: id={}", saved.idClient)
        return saved
    }

    // ─────────────────────────────────────────────────────────────
    // FIND ALL — Buscar todos os clientes
    // ─────────────────────────────────────────────────────────────
    // Retorna a lista de todos os clientes ordenada por nome (A→Z).
    // O Controller converterá cada Client em ClientSummaryResponse,
    // retornando apenas id e nome ao cliente HTTP.
    fun findAll(): List<Client> =
        repository.findAll(
            org.springframework.data.domain.Sort.by("nameClient").ascending()
        )

    // ─────────────────────────────────────────────────────────────
    // FIND BY ID (nullable) — Busca interna sem lançar exceção
    // ─────────────────────────────────────────────────────────────
    // Retorna null se não encontrado.
    // Usado internamente por findById (que lança exceção) e por outros métodos do Service.
    // findByIdOrNull é uma extensão do Spring Data que evita o Optional<> do Java.
    fun findByIdOrNull(id: Long): Client? = repository.findByIdOrNull(id)

    // ─────────────────────────────────────────────────────────────
    // FIND BY ID — Busca pública com exceção
    // ─────────────────────────────────────────────────────────────
    // Lança NotFoundException (HTTP 404) se o cliente não existir.
    // Centraliza a lógica de "não encontrado" em um único lugar.
    fun findById(id: Long): Client =
        findByIdOrNull(id) ?: throw NotFoundException("Cliente não encontrado: $id")

    // ─────────────────────────────────────────────────────────────
    // UPDATE — Alterar nameClient e contactClient
    // ─────────────────────────────────────────────────────────────
    // Parâmetros:
    //   id         → ID do cliente a alterar.
    //   name       → novo nome (vem do DTO).
    //   contact    → novo telefone (vem do DTO).
    //   updaterId  → ID do usuário autenticado que está fazendo a alteração.
    //
    // Retorna:
    //   Client atualizado  → se houve mudança em algum campo.
    //   null               → se nome E contato são iguais aos atuais (nada mudou).
    //
    // O Controller usa o retorno null para responder HTTP 204 No Content.
    fun update(id: Long, name: String, contact: String, updaterId: Long): Client? {
        val client = findById(id)

        // Verifica se há alguma mudança real antes de persistir.
        // Evita writes desnecessários no banco e retorna 204 ao invés de 200.
        if (client.nameClient == name && client.contactClient == contact) {
            return null
        }

        client.nameClient     = name
        client.contactClient  = contact
        // Atualiza quem fez a última alteração.
        // userCreateClient NÃO é tocado aqui (updatable = false também garante isso no JPA).
        client.userUpdateClient = updaterId

        repository.save(client)
        log.info("Cliente atualizado: id={} | por usuário={}", client.idClient, updaterId)
        return client
    }

    // ─────────────────────────────────────────────────────────────
    // DELETE — Remover cliente por ID
    // ─────────────────────────────────────────────────────────────
    // Lança NotFoundException se o cliente não existir (via findById).
    // Loga um aviso (warn) pois deleção é uma operação destrutiva.
    fun delete(id: Long) {
        val client = findById(id)
        log.warn("Cliente removido: id={}", client.idClient)
        repository.delete(client)
    }

    // companion object: equivalente a membros estáticos em Java.
    // O logger é compartilhado por todas as instâncias de ClientService.
    companion object {
        private val log = LoggerFactory.getLogger(ClientService::class.java)
    }
}