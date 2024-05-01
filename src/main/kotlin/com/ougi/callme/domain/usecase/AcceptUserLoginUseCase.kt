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
        addUserFlow(login)
            .flatMapConcat { result ->
                when (result) {
                    is RequestResult.Failure -> flowOf(result)
                    is RequestResult.Success<*> -> generateTokensFlow(login)
                }
            }
            .flatMapConcat { result ->
                when (result) {
                    is RequestResult.Failure -> flowOf(result)
                    is RequestResult.Success<*> -> addTurnUserFlow(
                        login = login,
                        password = (result.result as TokenPair).accessToken
                    ).map { result }
                }
            }
            .take(1)

    private fun addUserFlow(login: String) =
        flow { emit(dbUserRepository.addUser(login)) }
            .map { response ->
                if (response.status != HttpStatusCode.OK || response.status != HttpStatusCode.Conflict)
                    RequestResult.Success(Unit)
                else
                    RequestResult.Failure(response.status, response.readBytes())
            }

    private fun generateTokensFlow(login: String) =
        flow {
            emit(
                tokenizationRepository.generateTokenPair(
                    claimKey = LOGIN_PARAM_NAME,
                    claimValue = login
                )
            )
        }.map { response ->
            if (response.status == HttpStatusCode.OK)
                RequestResult.Success(
                    Json.decodeFromString<TokenPair>(response.readBytes().decodeToString())
                )
            else
                RequestResult.Failure(response.status, response.readBytes())
        }

    private fun addTurnUserFlow(
        login: String,
        password: String
    ) =
        flow {
            emit(
                dbTurnUserRepository.addUser(
                    login = login,
                    password = password,
                )
            )
        }
            .map { response ->
                if (response.status == HttpStatusCode.OK) RequestResult.Success(Unit)
                else RequestResult.Failure(response.status, response.readBytes())
            }


    private companion object {
        private const val LOGIN_PARAM_NAME = "login"
    }
}