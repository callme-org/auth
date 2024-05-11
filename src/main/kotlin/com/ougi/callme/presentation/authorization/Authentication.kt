package com.ougi.callme.presentation.authorization

import com.ougi.callme.domain.usecase.VerifyTokenUseCase
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject


fun Route.authenticate() {
    val verifyTokenUseCase by inject<VerifyTokenUseCase>()
    get("/authenticate") {
        call.request
            .parseAuthorizationHeader()
            ?.render()
            ?.split(" ")
            ?.get(1)
            ?.let { token -> verifyTokenUseCase.verifyAccessToken(token) }
            ?.let { isVerified ->
                if (isVerified) HttpStatusCode.OK to AUTHENTICATED_MESSAGE
                else HttpStatusCode.Unauthorized to NOT_AUTHENTICATED_MESSAGE
            }
            ?.let { (status, message) -> call.respond(status, message) }
            ?: call.respond(
                status = HttpStatusCode.Unauthorized,
                message = NOT_AUTHENTICATED_MESSAGE
            )
    }
}

private const val AUTHENTICATED_MESSAGE = "Authenticated"
private const val NOT_AUTHENTICATED_MESSAGE = "Token is not valid or has expired"