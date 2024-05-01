package com.ougi.callme.domain.usecase

import com.ougi.callme.domain.repository.TokenizationRepository
import io.ktor.http.*

interface VerifyTokenUseCase {

    suspend fun verifyAccessToken(token: String): Boolean

}

class VerifyTokenUseCaseImpl(
    private val tokenizationRepository: TokenizationRepository,
) : VerifyTokenUseCase {

    override suspend fun verifyAccessToken(token: String): Boolean =
        when (tokenizationRepository.verifyAccessToken(token).status) {
            HttpStatusCode.OK -> true
            else -> false
        }

}