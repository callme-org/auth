package com.ougi.callme.domain.model

import io.ktor.http.*

sealed class RequestResult(
    open val body: ByteArray
) {

    class Success(override val body: ByteArray) : RequestResult(body)

    class Failure(
        val status: HttpStatusCode,
        override val body: ByteArray,
    ) : RequestResult(body)
}