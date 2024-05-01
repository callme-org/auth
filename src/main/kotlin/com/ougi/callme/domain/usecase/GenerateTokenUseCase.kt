package com.ougi.callme.domain.usecase

import com.ougi.callme.domain.model.RequestResult
import com.ougi.callme.domain.repository.TokenizationRepository
import io.ktor.client.statement.*
import io.ktor.http.*

interface GenerateTokenUseCase {

    suspend fun generateToken(login: String): RequestResult

}

class GenerateTokenUseCaseImpl(
    private val tokenizationRepository: TokenizationRepository,
) : GenerateTokenUseCase {

    override suspend fun generateToken(login: String): RequestResult =
        tokenizationRepository.generateTokenPair(
            claimKey = LOGIN_PARAM_NAME,
            claimValue = login,
        )
            .let { response ->
                when (response.status) {
                    HttpStatusCode.OK ->
                        RequestResult.Success(response.readBytes())

                    else -> RequestResult.Failure(
                        status = response.status,
                        message = response.readBytes()
                    )
                }
            }

    private companion object {
        private const val LOGIN_PARAM_NAME = "login"
    }
}