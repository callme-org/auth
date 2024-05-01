package com.ougi.callme.domain.model

import io.ktor.http.*

sealed interface RequestResult {

    class Success(val result: ByteArray) : RequestResult

    class Failure(
        val status: HttpStatusCode,
        val message: ByteArray,
    ) : RequestResult
}