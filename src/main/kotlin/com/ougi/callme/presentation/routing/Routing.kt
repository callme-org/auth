package com.ougi.callme.presentation.routing

import com.ougi.callme.presentation.authorization.authenticate
import com.ougi.callme.presentation.authorization.authorization
import io.ktor.server.application.*
import io.ktor.server.routing.*

fun Application.configureRouting() =
    routing {
        authorization()
        authenticate()
    }