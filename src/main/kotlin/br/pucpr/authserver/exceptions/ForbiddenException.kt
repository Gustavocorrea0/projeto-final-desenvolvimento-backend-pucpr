package br.pucpr.authserver.exceptions

class ForbiddenException (
    message: String = "Forbidden",
    cause: Throwable? = null
) : IllegalStateException(message, cause) {
}