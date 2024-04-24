package com.ougi.callme.di

import com.ougi.callme.data.model.EnvironmentInfo
import com.ougi.callme.data.repository.JwtConfigRepositoryImpl
import com.ougi.callme.data.repository.KeyRepositoryImpl
import com.ougi.callme.domain.repository.JwtConfigRepository
import com.ougi.callme.domain.repository.KeyRepository
import com.ougi.callme.domain.usecase.JwtConfigUseCase
import com.ougi.callme.domain.usecase.JwtConfigUseCaseImpl
import io.ktor.client.*
import io.ktor.client.engine.*
import io.ktor.client.engine.java.*
import io.ktor.server.application.*
import io.ktor.server.config.*
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module


private val repositoriesModule = module {
    singleOf(::KeyRepositoryImpl) { bind<KeyRepository>() }
    singleOf(::JwtConfigRepositoryImpl) { bind<JwtConfigRepository>() }
}

private val useCasesModule = module {
    singleOf(::JwtConfigUseCaseImpl) { bind<JwtConfigUseCase>() }
}

private val presentationModule = module {
    single { HttpClient(Java) }
}

fun createEnvironmentModule(applicationConfig: ApplicationConfig) =
    module {
        single {
            EnvironmentInfo(
                host = applicationConfig.host,
                port = applicationConfig.port,
            )
        }
    }


val appModule = module {
    includes(
        repositoriesModule,
        useCasesModule,
        presentationModule,
    )
}
