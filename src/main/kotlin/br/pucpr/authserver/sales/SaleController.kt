package br.pucpr.authserver.sales

import br.pucpr.authserver.exceptions.ForbiddenException
import br.pucpr.authserver.parts.responses.PartSummaryResponse
import br.pucpr.authserver.sales.requests.CreateSaleRequest
import br.pucpr.authserver.sales.responses.SaleResponse
import br.pucpr.authserver.sales.responses.SaleSummaryResponse
import br.pucpr.authserver.security.UserToken
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.bind.annotation.RequestBody

@RestController
@RequestMapping("/sales")
class SaleController (
    private val service: SaleService,
) {

    @SecurityRequirement(name = "jwt-auth")
    @PreAuthorize("isAuthenticated()")
    @PostMapping
    fun insert(
        @RequestBody @Valid requestSale: CreateSaleRequest,
        auth: Authentication
    ): ResponseEntity<SaleResponse> {
        val token = auth.principal as? UserToken ?: throw ForbiddenException("Token is Invalid")
        val sale = requestSale.toSale(userCreateSale = token.id)
        val saleSaved = service.insert(sale)

        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(SaleResponse(saleSaved))
    }

    @SecurityRequirement(name = "jwt-auth")
    @PreAuthorize("isAuthenticated()")
    @GetMapping
    fun getAllSales(): ResponseEntity<List<SaleSummaryResponse>> =
        service.findAll()
            .map { SaleSummaryResponse(it) }
            .let { ResponseEntity.ok(it) }

    @SecurityRequirement(name = "jwt-auth")
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/{id}")
    fun getOneSaleById(
        @PathVariable id: Long
    ): ResponseEntity<SaleResponse> =
        service.findById(id)
            .let { SaleResponse(it) }
            .let { ResponseEntity.ok(it) }

}