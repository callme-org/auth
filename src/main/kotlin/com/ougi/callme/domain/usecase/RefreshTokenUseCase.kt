package com.ougi.callme.domain.usecase

import com.ougi.callme.domain.constant.ParamsConstants
import com.ougi.callme.domain.model.TokenPair

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

        return generateTokenPairUseCase.generateTokenPair(
            login = decodedToken.getClaim(ParamsConstants.LOGIN_PARAM_NAME).asString()
        )
    }


}