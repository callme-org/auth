package com.ougi.callme.domain.usecase

import com.ougi.callme.domain.model.JwtConfig
import com.ougi.callme.domain.repository.JwtConfigRepository
import com.ougi.callme.domain.repository.KeyRepository

interface JwtConfigUseCase {

    fun provideJwtConfig(): JwtConfig

}

class JwtConfigUseCaseImpl(
    private val keyRepository: KeyRepository,
    private val jwtConfigRepository: JwtConfigRepository,
) : JwtConfigUseCase {

    override fun provideJwtConfig(): JwtConfig =
        jwtConfigRepository.getJwtConfig(
            keyPair = keyRepository.keyPair
        )

}