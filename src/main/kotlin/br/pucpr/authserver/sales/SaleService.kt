package br.pucpr.authserver.sales

import br.pucpr.authserver.clients.Client
import br.pucpr.authserver.clients.ClientService
import br.pucpr.authserver.exceptions.BadRequestException
import br.pucpr.authserver.exceptions.InsufficientStockException
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
            val partValueById: BigDecimal =
                partService.findValuePartById(sale.idPartSale!!) ?: throw NotFoundException("Part Value Not Found")

            val finalValue = partValueById.multiply(BigDecimal(sale.qtnPartSale!!))

            if (!partService.validPartById(sale.idPartSale!!)) {
                log.info("Part with id = {} Not Found", sale.idPartSale!!)
                throw NotFoundException("Part Not Found")
            }

            if (!clientService.validClientById(sale.idClientSale!!)) {
                log.info("Client with id = {} Not Found", sale.idClientSale!!)
                throw NotFoundException("Client Not Found")
            }

            val removePart: Boolean? = partService.removeQuantityPartById(
                sale.idPartSale!!,
                sale.qtnPartSale!!
            )

            if (!removePart!!) {
                log.info("Fail remove Part with id = {} Not Found", sale.idPartSale!!)
                throw InsufficientStockException("The quantity of parts requested is not in stock")
            }

            sale.finalValueSale = finalValue
            val saleSaved = saleRepository.save(sale)
            log.info("Create Part with id = {}", saleSaved.idSale)
            return saleSaved

    }
    
    fun findAll(): List<Sale> {
        return saleRepository.findAll(
            org.springframework.data.domain.Sort.by("idSale").ascending()
        )
    }
    
    fun findById(idSale: Long): Sale =
        saleRepository.findByIdOrNull(idSale) ?: throw NotFoundException("Sale not found")

}