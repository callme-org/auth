package com.ougi.callme.presentation.authorization

import com.ougi.callme.domain.model.RequestResult
import com.ougi.callme.domain.usecase.RefreshTokenUseCase
import com.ougi.callme.presentation.authorization.model.RefreshData
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject


fun Route.refresh() {
    val refreshTokenUseCase by inject<RefreshTokenUseCase>()
    post("/refresh") {
        val refreshToken = call.receive<RefreshData>().refreshToken
        val refreshResponse = refreshTokenUseCase.refreshToken(refreshToken)
        when (refreshResponse) {
            is RequestResult.Success -> HttpStatusCode.OK
            is RequestResult.Failure -> refreshResponse.status
        }.let { status -> call.respond(status, refreshResponse.body) }
    }
}