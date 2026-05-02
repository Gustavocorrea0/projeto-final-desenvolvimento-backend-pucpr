package br.pucpr.authserver.clients

import jakarta.persistence.*
import java.time.ZonedDateTime

// @Entity: informa ao JPA que esta classe representa uma tabela no banco de dados.
// Cada instância de Client corresponde a uma linha na tabela "ClientTable".
@Entity
@Table(name = "ClientTable")
class Client(

    // @Id: marca este campo como chave primária da tabela.
    // @GeneratedValue: o banco gera o valor automaticamente (auto-increment).
    @Id @GeneratedValue
    var idClient: Long? = null,

    // @Column: personaliza o mapeamento da coluna.
    // nullable = false → o banco não aceita valores nulos.
    // length = 50    → limita o campo a 50 caracteres no banco.
    // A validação de tamanho mínimo fica no DTO (CreateClientRequest).
    @Column(nullable = false, length = 50)
    var nameClient: String,

    // Telefone do cliente guardado como String para preservar formatação
    // (ex: "(41) 99999-9999"). nullable = false garante que sempre seja informado.
    @Column(nullable = false)
    var contactClient: String,

    // Data e hora de criação com timezone.
    // ZonedDateTime armazena também o fuso horário (ex: America/Sao_Paulo, GMT-3).
    // updatable = false → o JPA nunca altera este campo após a inserção,
    // garantindo que a data de criação seja imutável.
    @Column(nullable = false, updatable = false)
    var dateTimeCreateClient: ZonedDateTime,

    // ID do usuário que CRIOU o cliente.
    // updatable = false → assim como a data de criação, nunca é alterado após o INSERT.
    @Column(nullable = false, updatable = false)
    var userCreateClient: Long,

    // ID do usuário que criou OU alterou o cliente por último.
    // Diferente de userCreateClient, este campo É atualizado a cada PATCH.
    @Column(nullable = false)
    var userUpdateClient: Long,

    // Flag que indica se o cliente está ativo no sistema.
    // Inicializado como true na criação; pode ser alterado via lógica futura
    // (ex: soft-delete em vez de remoção física).
    @Column(nullable = false)
    var activeClient: Boolean = true
)