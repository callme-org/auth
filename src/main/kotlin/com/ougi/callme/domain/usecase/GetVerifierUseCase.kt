package com.ougi.callme.domain.usecase

import com.auth0.jwt.JWT
import com.auth0.jwt.JWTVerifier
import com.auth0.jwt.algorithms.Algorithm
import com.ougi.callme.domain.repository.JwtConfigRepository
import com.ougi.callme.domain.repository.KeyRepository
import java.security.KeyPair
import java.security.interfaces.RSAPrivateKey
import java.security.interfaces.RSAPublicKey

interface GetVerifierUseCase {


    fun getAccessTokenVerifier(): JWTVerifier

    fun getRefreshTokenVerifier(): JWTVerifier

}

class GetVerifierUseCaseImpl(
    private val jwtConfigRepository: JwtConfigRepository,
    private val keyRepository: KeyRepository,
) : GetVerifierUseCase {

    override fun getAccessTokenVerifier() = createVerifier(keyRepository.accessKeyPair)

    override fun getRefreshTokenVerifier() = createVerifier(keyRepository.refreshKeyPair)

    private fun createVerifier(keyPair: KeyPair): JWTVerifier {
        val jwtConfig = jwtConfigRepository.getJwtConfig()
        return JWT.require(
            Algorithm.RSA256(
                keyPair.public as RSAPublicKey,
                keyPair.private as RSAPrivateKey
            )
        )
            .withAudience(jwtConfig.audience)
            .withIssuer(jwtConfig.issuer)
            .build()
    }

}