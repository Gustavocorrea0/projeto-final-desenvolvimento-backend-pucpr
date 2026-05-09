package br.pucpr.authserver.clients.requests

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size

// DTO de atualização do cliente.
// Apenas os campos que o usuário PODE alterar estão presentes.
//
// Campos EXCLUÍDOS intencionalmente (não podem ser alterados via PATCH):
//   - idClient              → chave primária, imutável.
//   - dateTimeCreateClient  → data de criação, imutável.
//   - userCreateClient      → quem criou, imutável.
//   - userUpdateClient      → preenchido automaticamente pelo Service com o usuário autenticado.
//   - activeClient          → gerenciado por lógica própria (não por este endpoint).

data class UpdateClientRequest(
    // @NotBlank garante que não aceita null nem string vazia.
    // @Size mantém a mesma regra de negócio da criação.
    @field:NotBlank(message = "The Client Name is Required")
    @field:Size(min = 1, max = 50, message = "The Name Must Be Between 1 and 50 Characters Long")
    val nameClient: String?,

    @field:NotBlank(message = "Client contact is required")
    val contactClient: List<ContactRequest>?
)