package com.ougi.callme.domain.usecase

import com.ougi.callme.domain.model.RequestResult
import com.ougi.callme.domain.model.TokenPair
import com.ougi.callme.domain.repository.DbTurnUserRepository
import com.ougi.callme.domain.repository.DbUserRepository
import com.ougi.callme.domain.repository.TokenizationRepository
import io.ktor.client.call.*
import io.ktor.client.statement.*
import io.ktor.http.*

interface AcceptUserLoginUseCase {

    suspend fun acceptUserLogin(login: String): RequestResult
}

class AcceptUserLoginUseCaseImpl(
    private val dbUserRepository: DbUserRepository,
    private val dbTurnUserRepository: DbTurnUserRepository,
    private val tokenizationRepository: TokenizationRepository,
) : AcceptUserLoginUseCase {

    override suspend fun acceptUserLogin(login: String): RequestResult {
        val addUserResponse = dbUserRepository.addUser(login)
        if (!listOf(HttpStatusCode.OK, HttpStatusCode.Conflict).contains(addUserResponse.status))
            return RequestResult.Failure(addUserResponse.status, addUserResponse.readBytes())

        val generateTokensResponse = tokenizationRepository.generateTokenPair(
            claimKey = LOGIN_PARAM_NAME,
            claimValue = login
        )
        if (generateTokensResponse.status != HttpStatusCode.OK)
            return RequestResult.Failure(generateTokensResponse.status, generateTokensResponse.readBytes())

        val addTurnUserResponse = dbTurnUserRepository.addUser(
            login = login,
            password = generateTokensResponse.body<TokenPair>().accessToken,
        )

        return if (addTurnUserResponse.status == HttpStatusCode.OK)
            RequestResult.Success(generateTokensResponse.readBytes())
        else
            RequestResult.Failure(addTurnUserResponse.status, addTurnUserResponse.readBytes())
    }

    private companion object {
        private const val LOGIN_PARAM_NAME = "login"
    }
}