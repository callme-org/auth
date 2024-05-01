package com.ougi.callme.domain.repository

import io.ktor.client.statement.*

interface DbUserRepository {

    suspend fun requestUserLogin(login: String): HttpResponse

}