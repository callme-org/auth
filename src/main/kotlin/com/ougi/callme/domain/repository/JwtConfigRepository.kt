package com.ougi.callme.domain.repository

import com.ougi.callme.domain.model.JwtConfig
import java.security.KeyPair

interface JwtConfigRepository {

    fun getJwtConfig(): JwtConfig

}