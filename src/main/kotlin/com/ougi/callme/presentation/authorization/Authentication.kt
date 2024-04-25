package com.ougi.callme.presentation.authorization

import com.ougi.callme.domain.constant.ParamsConstants.LOGIN_PARAM_NAME
import com.ougi.callme.domain.usecase.GetVerifierUseCase
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject

fun Application.configureAuthentication() {
    val getVerifierUseCase by inject<GetVerifierUseCase>()
    val accessTokenVerifier = getVerifierUseCase.getAccessTokenVerifier()
    authentication {
        jwt("auth-jwt") {
            verifier(accessTokenVerifier)
            validate()
            challenge()
        }
    }
}

fun Route.authenticate() =
    authenticate("auth-jwt") {
        get("/authenticate") {
            call.respond(
                status = HttpStatusCode.OK,
                message = "Authenticated"
            )
        }
    }

private fun JWTAuthenticationProvider.Config.validate() =
    validate { credential ->
        if (credential.payload.getClaim(LOGIN_PARAM_NAME).asString() != null)
            JWTPrincipal(credential.payload)
        else
            null
    }

private fun JWTAuthenticationProvider.Config.challenge() =
    challenge { _, _ ->
        call.respond(
            status = HttpStatusCode.Unauthorized,
            message = "Token is not valid or has expired"
        )
    }