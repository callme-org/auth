package com.ougi.callme.domain.model

import io.ktor.http.*

sealed interface RequestResult {

    class Success<T : Any>(val result: T) : RequestResult

    class Failure(
        val status: HttpStatusCode,
        val message: ByteArray,
    ) : RequestResult
}