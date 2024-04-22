package com.ougi.callme.data.repository

import com.ougi.callme.domain.repository.KeyRepository
import java.security.KeyPair
import java.security.KeyPairGenerator

class KeyRepositoryImpl : KeyRepository {

    override val keyPair: KeyPair by lazy(LazyThreadSafetyMode.NONE) {
        KeyPairGenerator.getInstance(RSA_ALGORITHM)
            .generateKeyPair()
    }

    private companion object {
        private const val RSA_ALGORITHM = "RSA"
    }
}