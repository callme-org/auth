package com.ougi.callme.domain.usecase

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.ougi.callme.domain.constant.ParamsConstants
import com.ougi.callme.domain.model.TokenPair
import com.ougi.callme.domain.repository.JwtConfigRepository
import com.ougi.callme.domain.repository.KeyRepository
import java.security.KeyPair
import java.security.interfaces.RSAPrivateKey
import java.security.interfaces.RSAPublicKey
import java.util.*
import java.util.concurrent.TimeUnit

interface GenerateTokenPairUseCase {

    fun generateTokenPair(login: String): TokenPair
}

class GenerateTokenPairUseCaseImpl(
    private val jwtConfigRepository: JwtConfigRepository,
    private val keyRepository: KeyRepository,
) : GenerateTokenPairUseCase {


    override fun generateTokenPair(login: String) =
        TokenPair(
            accessToken =
            createToken(
                login = login,
                tokenExpiringMillis = accessTokenExpiringMillis,
                keyPair = keyRepository.accessKeyPair,
            ),
            refreshToken =
            createToken(
                login = login,
                tokenExpiringMillis = refreshTokenExpiringMillis,
                keyPair = keyRepository.refreshKeyPair,
            )
        )


    private fun createToken(
        login: String,
        tokenExpiringMillis: Long,
        keyPair: KeyPair
    ): String {
        val jwtConfig = jwtConfigRepository.getJwtConfig()
        return JWT.create()
            .withAudience(jwtConfig.audience)
            .withIssuer(jwtConfig.issuer)
            .withClaim(ParamsConstants.LOGIN_PARAM_NAME, login)
            .withExpiresAt(Date(System.currentTimeMillis() + tokenExpiringMillis))
            .sign(
                Algorithm.RSA256(
                    keyPair.public as RSAPublicKey,
                    keyPair.private as RSAPrivateKey
                )
            )
    }

    private companion object {
        private val accessTokenExpiringMillis = TimeUnit.HOURS.toMillis(1)
        private val refreshTokenExpiringMillis = TimeUnit.DAYS.toMillis(7)
    }
}