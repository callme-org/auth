package com.ougi.callme.domain.repository

import io.ktor.client.statement.*

interface TokenizationRepository {

    suspend fun generateTokenPair(
        claimKey: String,
        claimValue: String,
    ): HttpResponse

    suspend fun refreshToken(
        token: String,
        claimKey: String,
    ): HttpResponse

    suspend fun verifyAccessToken(token: String): HttpResponse

}