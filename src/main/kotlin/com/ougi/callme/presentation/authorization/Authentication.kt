package com.ougi.callme.presentation.authorization

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.ougi.callme.domain.model.JwtConfig
import com.ougi.callme.domain.usecase.JwtConfigUseCase
import com.ougi.callme.presentation.authorization.ParamsConstants.LOGIN_PARAM_NAME
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject
import java.security.interfaces.RSAPrivateKey
import java.security.interfaces.RSAPublicKey

fun Application.configureAuthentication() {
    val jwtConfigUseCase by inject<JwtConfigUseCase>()
    val jwtConfig = jwtConfigUseCase.provideJwtConfig()
    authentication {
        jwt("auth-jwt") {
            verify(jwtConfig)
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


private fun JWTAuthenticationProvider.Config.verify(jwtConfig: JwtConfig) =
    verifier(
        JWT.require(
            Algorithm.RSA256(
                jwtConfig.keyPair.public as RSAPublicKey,
                jwtConfig.keyPair.private as RSAPrivateKey
            )
        )
            .withAudience(jwtConfig.audience)
            .withIssuer(jwtConfig.issuer)
            .build()
    )

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