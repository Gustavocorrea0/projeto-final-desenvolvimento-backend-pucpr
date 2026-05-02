package br.pucpr.authserver.clients

import br.pucpr.authserver.clients.requests.CreateClientRequest
import br.pucpr.authserver.clients.requests.UpdateClientRequest
import br.pucpr.authserver.clients.responses.ClientResponse
import br.pucpr.authserver.clients.responses.ClientSummaryResponse
import br.pucpr.authserver.exceptions.ForbiddenException
import br.pucpr.authserver.security.UserToken
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.*

// @RestController: combina @Controller + @ResponseBody.
// Indica que esta classe recebe requisições HTTP e retorna dados (JSON) diretamente no body.
//
// @RequestMapping("/clients"): define o prefixo de URL para todos os endpoints desta classe.
// Todos os métodos abaixo herdam esse caminho base.
@RestController
@RequestMapping("/clients")
class ClientController(
    // O Spring injeta automaticamente o ClientService via construtor.
    private val service: ClientService
) {

    // ─────────────────────────────────────────────────────────────
    // POST /clients — Criar um novo cliente
    // ─────────────────────────────────────────────────────────────
    // Requer autenticação (qualquer usuário logado pode criar).
    // O token JWT é lido via Authentication para identificar quem está criando.
    //
    // @SecurityRequirement: informa ao Swagger que este endpoint requer JWT.
    // @PreAuthorize("isAuthenticated()"): bloqueia requisições sem token válido (HTTP 401/403).
    // @RequestBody @Valid: desserializa o JSON e dispara as validações do DTO antes de entrar no método.
    // auth: Authentication: objeto injetado pelo Spring Security com os dados do usuário logado.
    @SecurityRequirement(name = "jwt-auth")
    @PreAuthorize("isAuthenticated()")
    @PostMapping
    fun insert(
        @RequestBody @Valid request: CreateClientRequest,
        auth: Authentication
    ): ResponseEntity<ClientResponse> {
        // Extrai o UserToken do principal do token JWT.
        // Se por algum motivo o principal não for UserToken, lança ForbiddenException (HTTP 403).
        val token = auth.principal as? UserToken
            ?: throw ForbiddenException("Token inválido.")

        // Converte o DTO em entidade, passando o ID do usuário autenticado.
        // A data de criação e o status ativo são preenchidos dentro de toClient().
        val client = request.toClient(userCreateId = token.id)

        val saved = service.insert(client)

        // HTTP 201 Created: padrão REST para criação bem-sucedida de recursos.
        // O body retorna a resposta completa com todos os campos do cliente criado.
        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(ClientResponse(saved))
    }

    // ─────────────────────────────────────────────────────────────
    // GET /clients — Listar todos os clientes (resumido)
    // ─────────────────────────────────────────────────────────────
    // Endpoint público (não requer autenticação), assim como o GET de users no projeto original.
    // Retorna apenas ID e nome (ClientSummaryResponse) para economizar dados na listagem.
    @GetMapping
    fun list(): ResponseEntity<List<ClientSummaryResponse>> =
        service.findAll()
            .map { ClientSummaryResponse(it) }   // converte cada Client em DTO resumido
            .let { ResponseEntity.ok(it) }        // empacota na resposta HTTP 200

    // ─────────────────────────────────────────────────────────────
    // GET /clients/{id} — Buscar um único cliente (completo)
    // ─────────────────────────────────────────────────────────────
    // @PathVariable: lê o {id} diretamente da URL (ex: GET /clients/42 → id = 42).
    // Se não encontrado, o Service lança NotFoundException → Spring retorna HTTP 404.
    @GetMapping("/{id}")
    fun getById(
        @PathVariable id: Long
    ): ResponseEntity<ClientResponse> =
        service.findById(id)
            .let { ClientResponse(it) }     // converte para o DTO completo
            .let { ResponseEntity.ok(it) }  // HTTP 200 com o body

    // ─────────────────────────────────────────────────────────────
    // PATCH /clients/{id} — Alterar nameClient e contactClient
    // ─────────────────────────────────────────────────────────────
    // Requer autenticação. Qualquer usuário logado pode alterar (não restrito a ADMIN).
    // O ID do usuário que está alterando é capturado do token JWT e passado ao Service,
    // que o armazena em userUpdateClient.
    //
    // PATCH vs PUT:
    //   PATCH → atualização parcial (apenas os campos enviados mudam).
    //   PUT   → substituição total (todos os campos devem ser enviados).
    //   Aqui usamos PATCH pois só alteramos nome e contato.
    @SecurityRequirement(name = "jwt-auth")
    @PreAuthorize("isAuthenticated()")
    @PatchMapping("/{id}")
    fun update(
        @PathVariable id: Long,
        @RequestBody @Valid request: UpdateClientRequest,
        auth: Authentication
    ): ResponseEntity<ClientResponse> {
        val token = auth.principal as? UserToken
            ?: throw ForbiddenException("Token inválido.")

        // Chama o Service passando: id do cliente, novos valores e id do editor.
        // Se retornar null, nenhum campo mudou → HTTP 204 No Content (sem body).
        // Se retornar o cliente, houve alteração → HTTP 200 com o cliente atualizado.
        return service.update(
            id        = id,
            name      = request.nameClient!!,
            contact   = request.contactClient!!,
            updaterId = token.id
        )
            ?.let { ClientResponse(it) }
            ?.let { ResponseEntity.ok(it) }
            ?: ResponseEntity.noContent().build()
    }

    // ─────────────────────────────────────────────────────────────
    // DELETE /clients/{id} — Remover cliente por ID
    // ─────────────────────────────────────────────────────────────
    // Restrito a usuários com papel ADMIN (mesma regra do delete de User).
    // @PreAuthorize("hasRole('ADMIN')"): o Spring Security verifica o token antes de entrar no método.
    // Se o cliente não existir, o Service lança NotFoundException → HTTP 404.
    // Em caso de sucesso: HTTP 200 sem body (ResponseEntity<Void>).
    @SecurityRequirement(name = "jwt-auth")
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    fun delete(
        @PathVariable id: Long
    ): ResponseEntity<Void> {
        service.delete(id)
        return ResponseEntity.ok().build()
    }
}