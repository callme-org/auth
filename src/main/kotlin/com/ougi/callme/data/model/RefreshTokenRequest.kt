package com.ougi.callme.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
class RefreshTokenRequest(
    @SerialName("refreshToken")
    val refreshToken: String,
    @SerialName("claim")
    val claim: String,
)