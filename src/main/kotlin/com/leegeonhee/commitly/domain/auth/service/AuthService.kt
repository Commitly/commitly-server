package com.leegeonhee.commitly.domain.auth.service

import com.leegeonhee.commitly.domain.auth.domain.model.OAuthTokensResponse
import org.springframework.stereotype.Service


interface AuthService {
    fun getAccessToken(code: String): OAuthTokensResponse
}