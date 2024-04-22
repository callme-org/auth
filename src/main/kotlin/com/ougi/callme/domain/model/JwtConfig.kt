package com.ougi.callme.domain.model

import java.security.KeyPair

class JwtConfig(
    val audience: String,
    val keyPair: KeyPair,
    val issuer: String,
)