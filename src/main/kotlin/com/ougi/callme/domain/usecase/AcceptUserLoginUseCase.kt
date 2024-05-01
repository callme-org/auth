package com.ougi.callme.domain.usecase

import com.ougi.callme.domain.model.RequestResult
import com.ougi.callme.domain.model.TokenPair
import com.ougi.callme.domain.repository.DbTurnUserRepository
import com.ougi.callme.domain.repository.DbUserRepository
import com.ougi.callme.domain.repository.TokenizationRepository
import io.ktor.client.statement.*
import io.ktor.http.*
import kotlinx.coroutines.flow.*
import kotlinx.serialization.json.Json

interface AcceptUserLoginUseCase {

    fun acceptUserLoginFlow(login: String): Flow<RequestResult>
}

class AcceptUserLoginUseCaseImpl(
    private val dbUserRepository: DbUserRepository,
    private val dbTurnUserRepository: DbTurnUserRepository,
    private val tokenizationRepository: TokenizationRepository,
) : AcceptUserLoginUseCase {

    override fun acceptUserLoginFlow(login: String): Flow<RequestResult> =
        flow { emit(dbUserRepository.addUser(login)) }
            .onEach { response ->
                if (response.status != HttpStatusCode.OK || response.status != HttpStatusCode.Conflict)
                    throw RequestException(response.status, response.readBytes())
            }
            .map {
                val generateTokenResponse = tokenizationRepository.generateTokenPair(
                    claimKey = LOGIN_PARAM_NAME,
                    claimValue = login
                )
                if (generateTokenResponse.status == HttpStatusCode.OK)
                    Json.decodeFromString<TokenPair>(generateTokenResponse.readBytes().decodeToString())
                else
                    throw RequestException(generateTokenResponse.status, generateTokenResponse.readBytes())
            }
            .onEach { tokens ->
                dbTurnUserRepository.addUser(
                    login = login,
                    token = tokens.accessToken,
                )
            }
            .take(1)
            .map { tokens -> RequestResult.Success(tokens) as RequestResult }
            .catch { th ->
                emit(
                    RequestResult.Failure(
                        status = (th as? RequestException)?.status ?: HttpStatusCode.BadRequest,
                        message = (th as? RequestException)?.messageBytes ?: byteArrayOf()
                    )
                )
            }


    private class RequestException(
        val status: HttpStatusCode,
        val messageBytes: ByteArray
    ) : Exception(messageBytes.decodeToString())

    private companion object {
        private const val LOGIN_PARAM_NAME = "login"
    }
}