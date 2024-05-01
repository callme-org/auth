package com.ougi.callme.data.repository

import com.ougi.callme.data.model.TurnUserRequest
import com.ougi.callme.domain.repository.DbTurnUserRepository
import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*

class DbTurnUserRepositoryImpl(
    private val httpClient: HttpClient,
) : DbTurnUserRepository {

    override suspend fun addUser(login: String, password: String): HttpResponse =
        httpClient.post("http://callme-turn-user:8080/common/turnuser/create") {
            contentType(ContentType.Application.Json)
            setBody(
                TurnUserRequest(
                    login = login,
                    password = password
                )
            )
        }

}