package br.pucpr.authserver.clients

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

// @Repository: marca esta interface como componente de acesso a dados.
// O Spring a detecta no classpath e cria automaticamente uma implementação em tempo de execução.
//
// JpaRepository<Client, Long>:
//   → Client : a entidade gerenciada por este repositório.
//   → Long   : o tipo da chave primária (idClient).
//
// Ao estender JpaRepository, ganhamos gratuitamente:
//   save(), findById(), findAll(), deleteById(), count(), existsById(), entre outros.
// Não precisamos escrever SQL ou implementar nada — o Spring Data faz tudo.
@Repository
interface ClientRepository : JpaRepository<Client, Long>