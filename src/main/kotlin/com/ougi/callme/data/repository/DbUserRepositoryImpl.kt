package com.ougi.callme.data.repository

import com.ougi.callme.data.model.UserLogin
import com.ougi.callme.domain.repository.DbUserRepository
import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.http.*

class DbUserRepositoryImpl(
    private val httpClient: HttpClient
) : DbUserRepository {

    override suspend fun addUser(login: String) =
        httpClient.post("http://callme-user:8080/common/user/create") {
            contentType(ContentType.Application.Json)
            setBody(UserLogin(login))
        }

}