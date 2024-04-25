package com.ougi.callme.domain.usecase

import com.ougi.callme.domain.constant.ParamsConstants
import com.ougi.callme.domain.model.TokenPair
import java.util.*
import java.util.concurrent.TimeUnit

interface RefreshTokenUseCase {

    fun refreshToken(refreshToken: String): TokenPair

}

class RefreshTokenUseCaseImpl(
    private val getVerifierUseCase: GetVerifierUseCase,
    private val generateTokenPairUseCase: GenerateTokenPairUseCase,
) : RefreshTokenUseCase {

    override fun refreshToken(refreshToken: String): TokenPair {
        val refreshTokenVerifier = getVerifierUseCase.getRefreshTokenVerifier()
        val decodedToken = refreshTokenVerifier.verify(refreshToken)
        val isAboutToExpire = ((decodedToken.expiresAt.time - Date().time) / TimeUnit.DAYS.toMillis(1)) < 1
        val newTokenPair = generateTokenPairUseCase.generateTokenPair(
            login = decodedToken.getClaim(ParamsConstants.LOGIN_PARAM_NAME).asString()
        )
        return if (isAboutToExpire) newTokenPair
        else newTokenPair.copy(refreshToken = refreshToken)
    }


}