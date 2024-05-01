package com.ougi.callme.domain.usecase

import com.ougi.callme.domain.model.RequestResult
import com.ougi.callme.domain.repository.DbUserRepository
import com.ougi.callme.domain.repository.TokenizationRepository
import io.ktor.client.statement.*
import io.ktor.http.*

interface AcceptUserLoginUseCase {

    suspend fun acceptUserLogin(login: String): RequestResult
}

class AcceptUserLoginUseCaseImpl(
    private val dbUserRepository: DbUserRepository,
    private val tokenizationRepository: TokenizationRepository,
) : AcceptUserLoginUseCase {


    override suspend fun acceptUserLogin(login: String): RequestResult {
        val addUserResponse = dbUserRepository.addUser(login)
        return when (addUserResponse.status) {
            HttpStatusCode.OK, HttpStatusCode.Conflict -> {
                val generateTokenResponse = tokenizationRepository.generateTokenPair(
                    claimKey = LOGIN_PARAM_NAME,
                    claimValue = login
                )
                when (generateTokenResponse.status) {
                    HttpStatusCode.OK -> RequestResult.Success(generateTokenResponse.readBytes())
                    else -> RequestResult.Failure(generateTokenResponse.status, generateTokenResponse.readBytes())
                }
            }

            else -> RequestResult.Failure(addUserResponse.status, addUserResponse.readBytes())
        }
    }

    private companion object {
        private const val LOGIN_PARAM_NAME = "login"
    }
}