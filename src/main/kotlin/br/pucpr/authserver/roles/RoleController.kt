package br.pucpr.authserver.roles

import br.pucpr.authserver.roles.requests.CreateRoleRequest
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/roles")
class RoleController(val roleService: RoleService) {

    @PostMapping
    fun insert(
        @RequestBody
        @Valid
        role: CreateRoleRequest
    ) = roleService.insert(role.toRole())
                ?.let { ResponseEntity.ok(it) }
                ?.let { ResponseEntity.status(HttpStatus.CREATED).body(it) }
                ?: ResponseEntity.badRequest().build()

    @GetMapping
    fun list() = roleService.findAll().map {
        ResponseEntity.ok(it)
    }
}