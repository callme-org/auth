package com.ougi.callme.data.repository

import com.ougi.callme.data.model.TurnUserRequest
import com.ougi.callme.domain.repository.DbTurnUserRepository
import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.client.statement.*

class DbTurnUserRepositoryImpl(
    private val httpClient: HttpClient,
) : DbTurnUserRepository {

    override suspend fun addUser(login: String, token: String): HttpResponse =
        httpClient.post("http://callme-turn-user:8080/common/turnuser/create") {
            setBody(
                TurnUserRequest(
                    login = login,
                    password = token
                )
            )
        }

}