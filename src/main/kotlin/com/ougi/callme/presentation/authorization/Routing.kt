package com.ougi.callme.presentation.authorization

import io.ktor.server.application.*
import io.ktor.server.routing.*

fun Application.configureRouting() =
    routing {
        route("/open") {
            route("/auth") {
                login()
                authenticate()
            }
        }
    }
