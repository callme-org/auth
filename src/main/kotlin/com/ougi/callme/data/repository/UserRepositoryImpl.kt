package com.ougi.callme.data.repository

import com.ougi.callme.data.model.UserLogin
import com.ougi.callme.domain.repository.UserRepository
import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.http.*

class UserRepositoryImpl(
    private val httpClient: HttpClient
) : UserRepository {

    override suspend fun requestUserLogin(login: String) =
        httpClient.post("http://callme-user:8080/common/user/create") {
            contentType(ContentType.Application.Json)
            setBody(UserLogin(login))
        }

}