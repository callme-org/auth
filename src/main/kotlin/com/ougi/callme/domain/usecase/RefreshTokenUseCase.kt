package com.ougi.callme.domain.usecase

import com.ougi.callme.domain.model.RequestResult
import com.ougi.callme.domain.repository.TokenizationRepository
import io.ktor.client.statement.*
import io.ktor.http.*

interface RefreshTokenUseCase {

    suspend fun refreshToken(
        token: String,
    ): RequestResult

}

class RefreshTokenUseCaseImpl(
    private val tokenizationRepository: TokenizationRepository,
) : RefreshTokenUseCase {

    override suspend fun refreshToken(token: String) =
        tokenizationRepository.refreshToken(
            token = token,
            claimKey = LOGIN_PARAM_NAME
        ).let { response ->
            when (response.status) {
                HttpStatusCode.OK -> RequestResult.Success(response.readBytes())
                else -> RequestResult.Failure(response.status, response.readBytes())
            }
        }


    private companion object {
        private const val LOGIN_PARAM_NAME = "login"
    }
}