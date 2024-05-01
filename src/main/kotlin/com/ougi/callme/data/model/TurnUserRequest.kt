package com.ougi.callme.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
class TurnUserRequest(
    @SerialName("login")
    val login: String,
    @SerialName("password")
    val password: String,
)