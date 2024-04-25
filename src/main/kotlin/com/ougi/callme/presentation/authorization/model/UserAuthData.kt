package com.ougi.callme.presentation.authorization.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
class UserAuthData(
    @SerialName("login")
    val loginMd5: String,
    @SerialName("otp")
    val otpMd5: String
)