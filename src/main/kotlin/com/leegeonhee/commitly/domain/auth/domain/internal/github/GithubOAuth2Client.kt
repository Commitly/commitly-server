package com.leegeonhee.commitly.domain.auth.domain.internal.github

import com.leegeonhee.commitly.domain.auth.domain.model.GithubProperties
import com.leegeonhee.commitly.domain.auth.domain.model.OAuthAccessTokenRequest
import com.leegeonhee.commitly.domain.auth.domain.model.OAuthTokensResponse
import com.leegeonhee.commitly.gloabl.exception.CustomException
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.stereotype.Component
import org.springframework.web.client.HttpClientErrorException
import org.springframework.web.client.RestClient
import org.springframework.web.client.RestTemplate
import org.springframework.web.client.body
import org.springframework.web.client.toEntity

@Component
class GithubOAuth2Client(
    private val githubProperties: GithubProperties,
    private val restTemplate: RestTemplate
) {
    fun getAccessToken(code: String): OAuthTokensResponse? {
        val body = OAuthAccessTokenRequest(
            clientId = githubProperties.clientId?: throw CustomException(HttpStatus.UNAUTHORIZED, "조떗음"),
            clientSecret = githubProperties.clientSecret?:  throw CustomException(HttpStatus.UNAUTHORIZED, "조떗음"),
            code = code
        )
        val headers = HttpHeaders()
        headers.contentType = MediaType.APPLICATION_JSON

        val request = HttpEntity(body, headers)
        return  restTemplate.postForEntity(
            "https://github.com/login/oauth/access_token",request,OAuthTokensResponse::class.java
        ).body

    }
}