package br.pucpr.authserver.sales

import br.pucpr.authserver.clients.Client
import br.pucpr.authserver.clients.ClientService
import br.pucpr.authserver.exceptions.BadRequestException
import br.pucpr.authserver.exceptions.NotFoundException
import br.pucpr.authserver.parts.PartService
import org.slf4j.LoggerFactory
import org.springframework.data.repository.findByIdOrNull
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import java.math.BigDecimal

@Service
class SaleService (
    private val saleRepository: SaleRepository,
    private val partService: PartService,
    private val clientService: ClientService,
) {
    
    companion object {
        private val log = LoggerFactory.getLogger(ClientService::class.java)
    }

    fun insert(sale: Sale): Sale {
        try {
            val partValueById: BigDecimal =
                partService.findValuePartById(sale.idPartSale!!) ?: throw NotFoundException("Part Value Not Found")

            val finalValue = partValueById.multiply(BigDecimal(sale.qtnPartSale!!))

            sale.finalValueSale = finalValue

            if (!partService.validPartById(sale.idPartSale!!)) {
                throw NotFoundException("Part Not Found")
            }

            if (!clientService.validClientById(sale.idClientSale!!)) {
                throw NotFoundException("Client Not Found")
            }

            val saleSaved = saleRepository.save(sale)
            log.info("Create Part with id = {}", saleSaved.idSale)
            return saleSaved

        } catch (ex: Exception) {
            log.error("Error Insert Sale: {}", ex.message, ex)
            throw BadRequestException("Fail to insert Sale")
        }
    }
    
    fun findAll(): List<Sale> {
        return saleRepository.findAll(
            org.springframework.data.domain.Sort.by("idSale").ascending()
        )
    }
    
    fun findById(idSale: Long): Sale =
        saleRepository.findByIdOrNull(idSale) ?: throw NotFoundException("Sale not found")

}