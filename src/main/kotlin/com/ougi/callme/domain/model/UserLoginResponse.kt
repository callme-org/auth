package com.ougi.callme.domain.model

import io.ktor.http.*

sealed interface UserLoginResponse {

    data object Accepted : UserLoginResponse

    class Failure(
        val status: HttpStatusCode,
        val message: String,
    ) : UserLoginResponse

}