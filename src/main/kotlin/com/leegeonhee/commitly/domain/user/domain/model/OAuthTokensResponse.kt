package com.leegeonhee.commitly.domain.user.domain.model

import com.fasterxml.jackson.annotation.JsonProperty

data class OAuthTokensResponse(
    @JsonProperty("access_token")
    val accessToken: String? = null,
)