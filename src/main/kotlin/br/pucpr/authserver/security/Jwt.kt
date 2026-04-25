package br.pucpr.authserver.security

import br.pucpr.authserver.users.User
import br.pucpr.authserver.users.UserService.Companion.log
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.Jwts.claims
import io.jsonwebtoken.jackson.io.JacksonDeserializer
import io.jsonwebtoken.jackson.io.JacksonSerializer
import io.jsonwebtoken.security.Keys
import jakarta.servlet.http.HttpServletRequest
import org.h2.security.auth.Authenticator
import org.springframework.http.HttpHeaders.AUTHORIZATION
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.stereotype.Component
import java.time.ZoneOffset
import java.time.ZonedDateTime
import java.util.Date

@Component
class Jwt {
    fun crateToken(user: User) =
        UserToken(user).let {
            Jwts.builder().json(JacksonSerializer())
                .signWith(Keys.hmacShaKeyFor(SECRET.toByteArray()))
                .issuedAt(utcNow().toDate())
                .expiration(
                    utcNow().plusHours(
                        if (it.isAdmin) ADMIN_EXPIRE_HOURS else EXPIRE_HOURS
                    ).toDate()
                )
                .issuer(ISSUER)
                .subject(it.id.toString())
                .claim(USER_FIELD, it)
                .compact()
        }

    // decodificacao token
    fun extract(req: HttpServletRequest): Authentication? {
        try {
            val header = req.getHeader(AUTHORIZATION)
            if (header == null || !header.startsWith("Bearer ")) {
                log.debug("Token not found")
                return null
            }
            val token = header.substring(7).trim()

            val claims = Jwts.parser().json(
                JacksonDeserializer(
                    mapOf(USER_FIELD to UserToken::class.java)
                )
            ).verifyWith(Keys.hmacShaKeyFor(SECRET.toByteArray()))
                .build()
                .parseClaimsJws(token).payload

            if (claims.issuer != ISSUER) {
                log.debug("Invalid Issuer")
                return null
            }

            val userToken = claims.get(USER_FIELD,UserToken::class.java).toAuthentication()

            return userToken

        } catch (e: Throwable) {
            log.debug(e.message)
            return null
        }
    }

    // aula 4 - comecar
    companion object {
        const val SECRET = "9b4b5b1f9c3b50b4c847030d4ec942fb0cca5cd9"
        const val ADMIN_EXPIRE_HOURS = 1L
        const val EXPIRE_HOURS = 48L
        const val ISSUER = "AuthServer" // criador do token
        const val USER_FIELD = "user"

        private fun utcNow() = ZonedDateTime.now(ZoneOffset.UTC)
        private fun ZonedDateTime.toDate(): Date = Date.from(this.toInstant())
        private fun UserToken.toAuthentication(): Authentication {
            val authorities = roles.map { SimpleGrantedAuthority("ROLE_$it") }
            return UsernamePasswordAuthenticationToken.authenticated(
                this,
                id,
                authorities
            )
        }
    }

}