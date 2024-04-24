package com.ougi.callme.presentation.authorization

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.ougi.callme.domain.model.JwtConfig
import com.ougi.callme.domain.model.UserAuthData
import com.ougi.callme.domain.usecase.JwtConfigUseCase
import com.ougi.callme.presentation.authorization.ParamsConstants.LOGIN_PARAM_NAME
import com.ougi.callme.presentation.authorization.ParamsConstants.TOKEN_PARAM_NAME
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.util.pipeline.*
import org.koin.ktor.ext.inject
import java.security.MessageDigest
import java.security.interfaces.RSAPrivateKey
import java.security.interfaces.RSAPublicKey
import java.util.*
import java.util.concurrent.TimeUnit

fun Route.login() {

    val jwtConfigUseCase by inject<JwtConfigUseCase>()
    val jwtConfig = jwtConfigUseCase.provideJwtConfig()
    val tokenExpiringMillis = TimeUnit.HOURS.toMillis(1)

    post("/login") {
        val userAuthData = call.receive<UserAuthData>()

        //TODO подлкючить к otp cервису
        if (userAuthData.otpMd5 != "1234".toMd5Hex()) {
            call.respond(HttpStatusCode.Forbidden, "Invalid or expired otp")
            return@post
        }

        sendToken(
            jwtConfig = jwtConfig,
            loginMd5 = userAuthData.loginMd5,
            tokenExpiringMillis = tokenExpiringMillis
        )
    }
}

private suspend fun PipelineContext<Unit, ApplicationCall>.sendToken(
    jwtConfig: JwtConfig,
    loginMd5: String,
    tokenExpiringMillis: Long,
) =
    call.respond(
        hashMapOf(
            TOKEN_PARAM_NAME to JWT.create()
                .withAudience(jwtConfig.audience)
                .withIssuer(jwtConfig.issuer)
                .withClaim(LOGIN_PARAM_NAME, loginMd5)
                .withExpiresAt(Date(System.currentTimeMillis() + tokenExpiringMillis))
                .sign(
                    Algorithm.RSA256(
                        jwtConfig.keyPair.public as RSAPublicKey,
                        jwtConfig.keyPair.private as RSAPrivateKey
                    )
                )
        )
    )

@OptIn(ExperimentalStdlibApi::class)
private fun String.toMd5Hex() =
    MessageDigest.getInstance("MD5")
        .digest(toByteArray())
        .toHexString()


