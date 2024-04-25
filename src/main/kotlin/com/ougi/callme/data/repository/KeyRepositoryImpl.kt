package com.ougi.callme.data.repository

import com.ougi.callme.domain.repository.KeyRepository
import java.security.KeyPair
import java.security.KeyPairGenerator

class KeyRepositoryImpl : KeyRepository {

    private val keyPairGenerator by lazy { KeyPairGenerator.getInstance(RSA_ALGORITHM) }

    override val accessKeyPair: KeyPair by lazy { keyPairGenerator.generateKeyPair() }

    override val refreshKeyPair: KeyPair by lazy { keyPairGenerator.generateKeyPair() }

    private companion object {
        private const val RSA_ALGORITHM = "RSA"
    }
}