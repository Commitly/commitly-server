package com.leegeonhee.commitly.domain.auth.domain.model

import com.fasterxml.jackson.annotation.JsonProperty

// OAuthAccessTokenRequest.kt
data class OAuthAccessTokenRequest(
    @JsonProperty("client_id")
    val clientId: String,
    @JsonProperty("client_secret")
    val clientSecret: String,
    val code: String
)