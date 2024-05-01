package com.ougi.callme.domain.repository

import io.ktor.client.statement.*

interface DbTurnUserRepository {

    suspend fun addUser(
        login: String,
        token: String,
    ): HttpResponse
}