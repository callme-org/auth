package com.ougi.callme.presentation

import com.ougi.callme.di.appModule
import com.ougi.callme.di.createEnvironmentModule
import com.ougi.callme.presentation.authorization.configureAuthentication
import com.ougi.callme.presentation.routing.configureRouting
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.plugins.contentnegotiation.*
import org.koin.ktor.plugin.Koin

fun main() {
    embeddedServer(
        factory = Netty,
        port = 80,
        module = Application::module,
    )
        .start(wait = true)
}

private fun Application.module() {
    installPlugins()
    configure()
}

private fun Application.configure() {
    configureAuthentication()
    configureRouting()
}

private fun Application.installPlugins() {
    install(ContentNegotiation) { json() }
    install(Koin) {
        modules(
            appModule,
            createEnvironmentModule(environment.config)
        )
    }
}
