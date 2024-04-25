package com.ougi.callme.domain.model

import java.security.KeyPair

class VerifyConfig(
    val audience: String,
    val issuer: String,
    val accessKeyPair: KeyPair
)