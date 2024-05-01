package com.ougi.callme.presentation.authorization

import com.ougi.callme.domain.model.RequestResult
import com.ougi.callme.domain.usecase.AcceptUserLoginUseCase
import com.ougi.callme.presentation.authorization.model.UserAuthData
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.koin.ktor.ext.inject
import java.security.MessageDigest

fun Route.login() {

    val acceptUserLoginUseCase by inject<AcceptUserLoginUseCase>()

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

        acceptUserLoginUseCase.acceptUserLoginFlow(userAuthData.loginMd5)
            .onEach { result ->
                when (result) {
                    is RequestResult.Failure -> result.status to result.message
                    is RequestResult.Success<*> -> HttpStatusCode.OK to result.result
                }.let { (status, message) -> call.respond(status, message) }
            }
            .launchIn(this)
    }
}

@OptIn(ExperimentalStdlibApi::class)
private fun String.toMd5Hex() =
    MessageDigest.getInstance("MD5")
        .digest(toByteArray())
        .toHexString()


