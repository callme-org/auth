package com.ougi.callme.presentation.authorization

import com.ougi.callme.domain.model.RequestResult
import com.ougi.callme.domain.usecase.AcceptUserLoginUseCase
import com.ougi.callme.domain.usecase.GenerateTokenUseCase
import com.ougi.callme.presentation.authorization.model.UserAuthData
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.util.pipeline.*
import org.koin.ktor.ext.inject
import java.security.MessageDigest

fun Route.login() {

    val acceptUserLoginUseCase by inject<AcceptUserLoginUseCase>()
    val generateTokenUseCase by inject<GenerateTokenUseCase>()

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

        suspend fun loginRequest(
            onSuccess: suspend (RequestResult.Success) -> Unit,
            onFailure: suspend (RequestResult.Failure) -> Unit
        ) = createRequest(
            onSuccess = onSuccess,
            onFailure = onFailure,
            source = { acceptUserLoginUseCase.acceptUserLogin(userAuthData.loginMd5) }
        )


        suspend fun tokenRequest(
            onSuccess: suspend (RequestResult.Success) -> Unit,
            onFailure: suspend (RequestResult.Failure) -> Unit
        ) = createRequest(
            onSuccess = onSuccess,
            onFailure = onFailure,
            source = { generateTokenUseCase.generateToken(userAuthData.loginMd5) }
        )

        loginRequest(
            onSuccess = {
                tokenRequest(
                    onSuccess = { result ->
                        call.respond(
                            status = HttpStatusCode.OK,
                            message = result.result
                        )
                    },
                    onFailure = ::failureRespond
                )
            },
            onFailure = ::failureRespond
        )
    }
}

private suspend fun createRequest(
    onSuccess: suspend (RequestResult.Success) -> Unit,
    onFailure: suspend (RequestResult.Failure) -> Unit,
    source: suspend () -> RequestResult,
) =
    when (val response = source.invoke()) {
        is RequestResult.Success -> onSuccess(response)
        is RequestResult.Failure -> onFailure(response)
    }

private suspend fun PipelineContext<Unit, ApplicationCall>.failureRespond(failureResult: RequestResult.Failure) =
    call.respond(
        status = failureResult.status,
        message = failureResult.message
    )


@OptIn(ExperimentalStdlibApi::class)
private fun String.toMd5Hex() =
    MessageDigest.getInstance("MD5")
        .digest(toByteArray())
        .toHexString()


