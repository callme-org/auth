package com.ougi.callme.presentation.authorization.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
class RefreshData(
    @SerialName("refreshToken")
    val refreshToken: String
)