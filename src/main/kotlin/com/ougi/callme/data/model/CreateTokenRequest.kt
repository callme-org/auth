package com.ougi.callme.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
class CreateTokenRequest(
    @SerialName("claim")
    val claim: Claim,
)