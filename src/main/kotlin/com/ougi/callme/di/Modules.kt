package com.ougi.callme.di

import com.ougi.callme.data.repository.DbTurnUserRepositoryImpl
import com.ougi.callme.data.repository.DbUserRepositoryImpl
import com.ougi.callme.data.repository.TokenizationRepositoryImpl
import com.ougi.callme.domain.repository.DbTurnUserRepository
import com.ougi.callme.domain.repository.DbUserRepository
import com.ougi.callme.domain.repository.TokenizationRepository
import com.ougi.callme.domain.usecase.*
import io.ktor.client.*
import io.ktor.client.engine.java.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.serialization.kotlinx.json.*
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module


private val repositoriesModule = module {
    singleOf(::DbUserRepositoryImpl) { bind<DbUserRepository>() }
    singleOf(::TokenizationRepositoryImpl) { bind<TokenizationRepository>() }
    singleOf(::DbTurnUserRepositoryImpl) { bind<DbTurnUserRepository>() }
    single {
        HttpClient(Java) {
            install(ContentNegotiation) {
                json()
            }
        }
    }
}

private val useCasesModule = module {
    singleOf(::AcceptUserLoginUseCaseImpl) { bind<AcceptUserLoginUseCase>() }
    singleOf(::VerifyTokenUseCaseImpl) { bind<VerifyTokenUseCase>() }
    singleOf(::RefreshTokenUseCaseImpl) { bind<RefreshTokenUseCase>() }
}


val appModule = module {
    includes(
        repositoriesModule,
        useCasesModule,
    )
}
