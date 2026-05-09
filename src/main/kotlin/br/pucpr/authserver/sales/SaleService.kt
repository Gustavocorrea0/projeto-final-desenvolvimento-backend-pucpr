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

        var finalValue = BigDecimal.ZERO

        sale.salesParts.forEach { part ->

            val partValue: BigDecimal =
                partService.findValuePartById(part.idPartSale!!)
                    ?: throw NotFoundException("Part Value Not Found")

            if (!partService.validPartById(part.idPartSale!!)) {
                log.info("Part with id = {} Not Found", part.idPartSale!!)
                throw NotFoundException("Part Not Found")
            }

            val removePart: Boolean? = partService.removeQuantityPartById(
                part.idPartSale!!,
                part.qtnPartSale!!
            )

            if (!removePart!!) {
                log.info("Fail remove Part with id = {} Not Found", part.idPartSale!!)
                throw InsufficientStockException("The part with ID = ${part.idPartSale!!} does not have the requested quantity in stock.")
            }

            partValue.multiply(BigDecimal(part.qtnPartSale!!))

            // finalizar regra de calculo
            finalValue += partValue.multiply(BigDecimal(part.qtnPartSale!!))
        }

            if (!clientService.validClientById(sale.idClientSale!!)) {
                log.info("Client with id = {} Not Found", sale.idClientSale!!)
                throw NotFoundException("Client Not Found")
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