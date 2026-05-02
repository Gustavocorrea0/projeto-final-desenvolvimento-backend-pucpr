package br.pucpr.authserver.clients.requests

import br.pucpr.authserver.clients.Client
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size
import java.time.ZoneId
import java.time.ZonedDateTime

/*
    DTO (Data Transfer Object) de criação do cliente.
    Esta classe representa exatamente o corpo JSON que o cliente HTTP deve enviar.
    Ela é responsável por:
        1. Receber e validar os dados da requisição.
        2. Converter esses dados para a entidade Client (metodo toClient).

    Campos automáticos (NÃO estão aqui, pois não vêm do JSON):
       - idClient             → gerado pelo banco
       - dateTimeCreateClient → preenchido por toClient()
       - userCreateClient     → recebido via token JWT no Service
       - userUpdateClient     → recebido via token JWT no Service
       - activeClient         → sempre true na criação
*/

data class CreateClientRequest(

    // @NotBlank: rejeita null, string vazia ("") e string só com espaços ("   ").
    // @Size: valida o tamanho da string após trimming.
    //   min = 1  → pelo menos 1 caractere real.
    //   max = 50 → no máximo 50 caracteres (espelhando o @Column(length = 50) na entidade).
    @field:NotBlank(message = "O nome do cliente é obrigatório.")
    @field:Size(min = 1, max = 50, message = "O nome deve ter entre 1 e 50 caracteres.")
    val nameClient: String?,

    // Telefone é obrigatório, mas não validamos o formato aqui.
    // Poderíamos adicionar @Pattern(regexp = "...") futuramente se necessário.
    @field:NotBlank(message = "O contato do cliente é obrigatório.")
    val contactClient: String?
) {
    // Converte o DTO para a entidade Client, preenchendo os campos automáticos.
    //
    // Parâmetros recebidos do Service (não do JSON):
    //   userCreateId → ID do usuário autenticado que está criando o cliente.
    //
    // Zona de São Paulo: "America/Sao_Paulo" equivale a GMT-3 (com ajuste para horário de verão).
    // ZonedDateTime.now(zone) captura o instante atual já com o fuso horário correto.
    fun toClient(userCreateId: Long): Client = Client(
        nameClient           = nameClient!!,
        contactClient        = contactClient!!,
        dateTimeCreateClient = ZonedDateTime.now(ZoneId.of("America/Sao_Paulo")),
        userCreateClient     = userCreateId,  // quem criou — nunca muda
        userUpdateClient     = userCreateId,  // na criação, criador = último editor
        activeClient         = true           // sempre ativo ao ser criado
    )
}