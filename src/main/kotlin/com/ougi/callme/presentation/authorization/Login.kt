package com.ougi.callme.presentation.authorization

import com.ougi.callme.presentation.authorization.model.UserAuthData
import com.ougi.callme.domain.usecase.GenerateTokenPairUseCase
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject
import java.security.MessageDigest

fun Route.login() {

    val generateTokenPairUseCase by inject<GenerateTokenPairUseCase>()

    post("/login") {
        val userAuthData = call.receive<UserAuthData>()

        //TODO подлкючить к otp cервису
        if (userAuthData.otpMd5 != "1234".toMd5Hex()) {
            call.respond(
                status = HttpStatusCode.Forbidden,
                message = "Invalid or expired otp",
            )
            return@post
        }
        call.respond(
            status = HttpStatusCode.OK,
            message = generateTokenPairUseCase.generateTokenPair(userAuthData.loginMd5)
        )
    }
}


@OptIn(ExperimentalStdlibApi::class)
private fun String.toMd5Hex() =
    MessageDigest.getInstance("MD5")
        .digest(toByteArray())
        .toHexString()


