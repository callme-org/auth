package com.ougi.callme.data.repository

import com.ougi.callme.data.model.UserLogin
import com.ougi.callme.domain.repository.UserRepository
import io.ktor.client.*
import io.ktor.client.request.*

class UserRepositoryImpl(
    private val httpClient: HttpClient
) : UserRepository {

    override suspend fun requestUserLogin(login: String) =
        httpClient.request("http://callme-user:8080/m/user/create") {
            setBody(UserLogin(login))
        }

}