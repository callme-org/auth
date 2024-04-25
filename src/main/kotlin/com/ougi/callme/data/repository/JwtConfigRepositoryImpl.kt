package com.ougi.callme.data.repository

import com.ougi.callme.data.model.EnvironmentInfo
import com.ougi.callme.domain.model.JwtConfig
import com.ougi.callme.domain.repository.JwtConfigRepository
import java.security.KeyPair

class JwtConfigRepositoryImpl(
    private val environmentInfo: EnvironmentInfo,
) : JwtConfigRepository {

    override fun getJwtConfig(): JwtConfig {
        val environmentPath = "${environmentInfo.host}:${environmentInfo.port}"

        return JwtConfig(
            audience = environmentPath,
            issuer = "$environmentPath/",
        )
    }

}