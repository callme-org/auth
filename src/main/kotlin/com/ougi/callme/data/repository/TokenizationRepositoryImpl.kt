package com.ougi.callme.data.repository

import com.ougi.callme.data.model.Claim
import com.ougi.callme.data.model.CreateTokenRequest
import com.ougi.callme.data.model.RefreshTokenRequest
import com.ougi.callme.data.model.VerifyTokenRequest
import com.ougi.callme.domain.repository.TokenizationRepository
import io.ktor.client.*
import io.ktor.client.request.*

class TokenizationRepositoryImpl(
    private val httpClient: HttpClient
) : TokenizationRepository {

    override suspend fun generateTokenPair(
        claimKey: String,
        claimValue: String
    ) =
        httpClient.post(TOKENIZATION_HOST + CREATE_PATH) {
            setBody(
                CreateTokenRequest(
                    claim = Claim(
                        name = claimKey,
                        value = claimValue,
                    )
                )
            )
        }

    override suspend fun refreshToken(
        token: String,
        claimKey: String,
    ) =
        httpClient.post(TOKENIZATION_HOST + REFRESH_PATH) {
            setBody(
                RefreshTokenRequest(
                    refreshToken = token,
                    claim = claimKey,
                )
            )
        }


    override suspend fun verifyAccessToken(token: String) =
        httpClient.post(TOKENIZATION_HOST + VERIFY_PATH) {
            setBody(VerifyTokenRequest(token))
        }

    private companion object {
        private const val TOKENIZATION_HOST = "http://callme-tokenization:8080/common/token"

        private const val CREATE_PATH = "/create"
        private const val REFRESH_PATH = "/refresh"
        private const val VERIFY_PATH = "/verify"
    }
}