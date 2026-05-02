package br.pucpr.authserver.parts

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface PartRepository : JpaRepository<Part, Long> {
}