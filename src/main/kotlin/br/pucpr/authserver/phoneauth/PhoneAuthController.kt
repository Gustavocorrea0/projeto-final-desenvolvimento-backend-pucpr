package br.pucpr.authserver.phoneauth

import br.pucpr.authserver.phoneauth.requests.ConfirmCodeRequest
import br.pucpr.authserver.phoneauth.requests.PhoneAuthRequest
import br.pucpr.authserver.phoneauth.responses.PhoneAuthResponse
import jakarta.validation.Valid
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/phone-auth")
class PhoneAuthController (
    private val phoneAuthService: PhoneAuthService
) {

    companion object {
        private val log = LoggerFactory.getLogger(PhoneAuthService::class.java)
    }

    @PostMapping
    fun authenticate(@RequestBody @Valid request: PhoneAuthRequest
    ): ResponseEntity<Any> {

        return when (val result = phoneAuthService.checkPhoneAndUUID(request.phoneNumber, request.uuidUser)) {
            is PhoneAuthService.AuthStep1Result.Authenticated ->
                ResponseEntity.ok(PhoneAuthResponse(result.phoneAuth))
            is PhoneAuthService.AuthStep1Result.CodeSent ->
                ResponseEntity.status(HttpStatus.ACCEPTED).body(mapOf("message" to "Código enviado via SMS. Confirme em /phone-auth/confirm."))
        }
    }

    @PostMapping("/confirm")
    fun confirmCode(
        @RequestBody @Valid request: ConfirmCodeRequest
    ): ResponseEntity<Any> {
        return when (val result = phoneAuthService.confirmCode(
            request.phoneNumber,
            request.code,
            request.uuidUser,
            request.nameUser
        )) {

            is PhoneAuthService.AuthStep2Result.Created ->
                ResponseEntity.status(HttpStatus.CREATED).body(PhoneAuthResponse(result.phoneAuth))

            is PhoneAuthService.AuthStep2Result.Updated ->
                ResponseEntity.status(HttpStatus.ACCEPTED).body(PhoneAuthResponse(result.phoneAuth))

        }
    }

}