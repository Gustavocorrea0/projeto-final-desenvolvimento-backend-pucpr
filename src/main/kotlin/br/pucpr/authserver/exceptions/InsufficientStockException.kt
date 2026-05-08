package br.pucpr.authserver.exceptions

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

@ResponseStatus(HttpStatus.NOT_FOUND)
class InsufficientStockException (
    message: String = "Insufficient stock",
    cause: Throwable? = null
) : RuntimeException(message, cause) {
    constructor(id: Long, cause: Throwable?) : this("Part id: $id insufficient stock", null)
}