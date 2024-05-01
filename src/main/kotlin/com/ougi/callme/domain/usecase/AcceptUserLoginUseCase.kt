package com.ougi.callme.domain.usecase

import com.ougi.callme.domain.model.RequestResult
import com.ougi.callme.domain.repository.DbUserRepository
import io.ktor.client.statement.*
import io.ktor.http.*

interface AcceptUserLoginUseCase {

    suspend fun acceptUserLogin(login: String): RequestResult
}

class AcceptUserLoginUseCaseImpl(
    private val dbUserRepository: DbUserRepository
) : AcceptUserLoginUseCase {

    override suspend fun acceptUserLogin(login: String): RequestResult {
        val response = dbUserRepository.requestUserLogin(login)
        return when (response.status) {
            HttpStatusCode.OK, HttpStatusCode.Conflict ->
                RequestResult.Success(response.readBytes())

            else ->
                RequestResult.Failure(
                    status = response.status,
                    message = response.readBytes()
                )
        }
    }


}