package br.pucpr.authserver.parts

import br.pucpr.authserver.exceptions.ForbiddenException
import br.pucpr.authserver.parts.requests.CreatePartRequest
import br.pucpr.authserver.parts.requests.UpdatePartRequest
import br.pucpr.authserver.parts.responses.PartResponse
import br.pucpr.authserver.parts.responses.PartSummaryResponse
import br.pucpr.authserver.security.UserToken
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/parts")
class PartController (
    private val service: PartService
) {

    @SecurityRequirement(name = "jwt-auth")
    @PreAuthorize("isAuthenticated()") // definir admin
    @PostMapping
    fun insert(
        @RequestBody @Valid requestPart: CreatePartRequest,
        auth: Authentication
    ): ResponseEntity<PartResponse> {
        val token = auth.principal as? UserToken ?: throw ForbiddenException("Token is Invalid")
        val part = requestPart.toPart(userCreatePart = token.id)
        val partSaved = service.insert(part)

        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(PartResponse(partSaved))
    }

    @GetMapping
    fun getAllParts(): ResponseEntity<List<PartSummaryResponse>> =
        service.findAll()
            .map { PartSummaryResponse(it) }
            .let { ResponseEntity.ok(it) }

    @GetMapping("/{id}")
    fun getOnePartById(
        @PathVariable id: Long
    ): ResponseEntity<PartResponse> =
        service.findById(id)
            .let { PartResponse(it) }
            .let { ResponseEntity.ok(it) }

    @SecurityRequirement(name = "jwt-auth")
    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/{id}")
    fun updatePartById(
        @PathVariable id: Long,
        @RequestBody @Valid requestPart: UpdatePartRequest,
        auth: Authentication
    ) : ResponseEntity<PartResponse> {
        val token = auth.principal as? UserToken ?: throw ForbiddenException("Token is Invalid")

        return service.update(
            id = id,
            namePart = requestPart.namePart!!,
            brandPart = requestPart.brandPart!!,
            descriptionPart =  requestPart.descriptionPart!!,
            quantityPart = requestPart.quantityPart!!,
            valuePart = requestPart.valuePart!!,
            updaterId = token.id
        )
            ?.let { PartResponse(it) }
            ?.let { ResponseEntity.ok(it) }
            ?: ResponseEntity.noContent().build()
    }

    @SecurityRequirement(name = "jwt-auth")
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    fun deletePartById(
        @PathVariable id: Long
    ): ResponseEntity<Void> {
        service.deleteById(id)
        return ResponseEntity.ok().build()
    }

}