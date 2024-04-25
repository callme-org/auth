package com.ougi.callme.domain.usecase

import com.ougi.callme.domain.model.UserLoginResponse
import com.ougi.callme.domain.repository.UserRepository
import io.ktor.client.statement.*
import io.ktor.http.*

interface AcceptUserLoginUseCase {

    suspend fun acceptUserLogin(login: String): UserLoginResponse
}

class AcceptUserLoginUseCaseImpl(
    private val userRepository: UserRepository
) : AcceptUserLoginUseCase {

    override suspend fun acceptUserLogin(login: String): UserLoginResponse {
        val response = userRepository.requestUserLogin(login)
        return when (response.status) {
            HttpStatusCode.OK, HttpStatusCode.Conflict -> UserLoginResponse.Accepted
            else ->
                UserLoginResponse.Failure(
                    status = response.status,
                    message = response.readBytes().decodeToString()
                )
        }
    }


}