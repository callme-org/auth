package com.ougi.callme.domain.repository

import java.security.KeyPair

interface KeyRepository {

    val accessKeyPair: KeyPair

    val refreshKeyPair:KeyPair

}