package com.leegeonhee.commitly.domain.auth.domain.model

import com.fasterxml.jackson.annotation.JsonProperty

data class OAuthTokensResponse(
    @JsonProperty("access_token")
    val accessToken: String
)