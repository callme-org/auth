package com.ougi.callme.domain.repository

import io.ktor.client.statement.*

interface UserRepository {

    suspend fun requestUserLogin(login: String): HttpResponse

}