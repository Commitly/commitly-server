package com.leegeonhee.commitly.domain.auth.service

import com.leegeonhee.commitly.domain.auth.domain.internal.github.GithubOAuth2Client
import com.leegeonhee.commitly.domain.auth.domain.model.GithubProperties
import com.leegeonhee.commitly.domain.auth.domain.model.OAuthAccessTokenRequest
import com.leegeonhee.commitly.domain.auth.domain.model.OAuthTokensResponse
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate
import org.springframework.web.client.exchange
import org.springframework.web.client.getForObject

@Service
class AuthServiceImpl(
    private val githubOAuth2Client: GithubOAuth2Client
) : AuthService {
    override fun getAccessToken(code: String): OAuthTokensResponse {
        return githubOAuth2Client.getAccessToken(code)?: OAuthTokensResponse(
            accessToken = "왜안됨?",
        )
    }
}