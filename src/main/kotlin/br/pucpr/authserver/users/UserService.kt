package br.pucpr.authserver.users

import br.pucpr.authserver.exceptions.BadRequestException
import br.pucpr.authserver.exceptions.NotFoundException
import br.pucpr.authserver.exceptions.UnauthorizedException
import br.pucpr.authserver.roles.RoleRepository
import br.pucpr.authserver.security.Jwt
import br.pucpr.authserver.users.responses.LoginResponse
import br.pucpr.authserver.users.responses.UserResponse
import org.slf4j.LoggerFactory
import org.springframework.data.domain.Sort
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service

@Service
class UserService(
    val repository: UserRepository,
    val roleRepository: RoleRepository,
    val jwt: Jwt
) {
    fun insert(user: User): User {
        if (repository.findByEmail(user.email) != null) {
            throw BadRequestException("User already exists.")
        }

        val saved =  repository.save(user)
        log.info("Adding User: {}", saved.id)
        return saved

    }

    fun findAll(dir: SortDir = SortDir.ASC) = when (dir) {
        SortDir.ASC -> repository.findAll(Sort.by("name").ascending())
        SortDir.DESC -> repository.findAll(Sort.by("name").descending())
    }

    fun findByIdOrNull(id: Long) = repository.findByIdOrNull(id)

    fun findById(id: Long) = findByIdOrNull(id) ?: throw NotFoundException(id.toString())

    fun update(id: Long, name: String): User? {
        val user = findById(id)
        if (user.name == name) {
            return null
        }
        user.name = name
        repository.save(user)
        return user
    }

    fun delete(id: Long) {
        val user = findById(id)
        if (user.isAdmin() && repository.findByRole("ADMIN").size == 1) {
            throw BadRequestException("Cannot delete user last user Administrator.")
        }
        log.warn("Deleted user: {}", user.id)
        repository.delete(user)
    }

    fun findByRole(roleName: String) = repository.findByRole(roleName.uppercase())

    fun addRole(id: Long, roleName: String): Boolean {
        val upperRole = roleName.uppercase()
        val user = findById(id)
        if (user.roles.any { it.name == upperRole }) return false

        val role = roleRepository.findByName(upperRole) ?: throw BadRequestException("Role $upperRole does not exist.")

        user.roles.add(role)
        repository.save(user)
        log.info("Added role: {} | User: {}", upperRole, user.id)
        return true
    }

    // JWT
    fun login(email: String, password: String): LoginResponse {
        val user = repository.findByEmail(email) ?: throw UnauthorizedException("User Not Found")
        if (user.password != password) {
            throw UnauthorizedException("Wrong Passwords")
        }

        log.info("User: {} logged in", user.id)
        return LoginResponse(
            token = jwt.crateToken(user),
            user = UserResponse(user)
        )
    }

    // criacao de logs
    companion object {
        val log = LoggerFactory.getLogger(UserService::class.java)
    }

}