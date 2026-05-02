package br.pucpr.authserver.parts

import br.pucpr.authserver.clients.ClientService
import br.pucpr.authserver.exceptions.NotFoundException
import org.springframework.stereotype.Service
import org.slf4j.LoggerFactory
import org.springframework.data.repository.findByIdOrNull
import java.math.BigDecimal
import java.time.ZoneId
import java.time.ZonedDateTime

@Service
class PartService(
    private val partRepository: PartRepository,
) {

    companion object {
        private val log = LoggerFactory.getLogger(ClientService::class.java)
    }

    fun insert(part: Part): Part {
        val partSaved = partRepository.save(part)
        log.info("Create Part with id = {}", partSaved.idPart)
        return partSaved
    }

    fun findAll(): List<Part> {
        return partRepository.findAll(
            org.springframework.data.domain.Sort.by("namePart").ascending()
        )
    }

    fun findById(id: Long): Part =
        partRepository.findByIdOrNull(id) ?: throw NotFoundException("Part is Not Found")

    fun update(
        id: Long,
        namePart: String,
        brandPart: String,
        descriptionPart: String,
        quantityPart: Long,
        valuePart: BigDecimal,
        updaterId: Long
    ): Part? {
        val part = findById(id)

        if ( part.namePart == namePart && part.brandPart == brandPart && part.descriptionPart == descriptionPart &&
            part.quantityPart == quantityPart && part.valuePart == valuePart) {
            return null
        }

        part.namePart = namePart
        part.brandPart = brandPart
        part.descriptionPart = descriptionPart
        part.quantityPart = quantityPart
        part.valuePart = valuePart

        part.dateTimeUpdatePart = ZonedDateTime.now(ZoneId.of("America/Sao_Paulo"))
        part.userUpdatePart = updaterId

        partRepository.save(part)

        log.info("Part-modified: id={} | User-modified={}", part.idPart, updaterId)
        return part

    }

    fun deleteById(id: Long) {
        val part = findById(id)
        log.warn("Part with id = {} is removed", part.idPart)
        partRepository.delete(part)
    }
}