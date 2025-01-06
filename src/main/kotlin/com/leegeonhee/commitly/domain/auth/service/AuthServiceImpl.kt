package com.leegeonhee.commitly.domain.auth.service

import com.leegeonhee.commitly.domain.auth.domain.internal.github.GithubOAuth2Client
import com.leegeonhee.commitly.domain.auth.domain.model.GithubProperties
import com.leegeonhee.commitly.domain.auth.domain.model.OAuthAccessTokenRequest
import com.leegeonhee.commitly.domain.auth.domain.model.OAuthTokensResponse
import com.leegeonhee.commitly.domain.auth.domain.model.user.GithubUserInfo
import com.leegeonhee.commitly.gloabl.exception.CustomException
import org.springframework.http.*
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate
import org.springframework.web.client.exchange
import org.springframework.web.client.getForObject

@Service
class AuthServiceImpl(
    private val githubOAuth2Client: GithubOAuth2Client
) : AuthService {
    override fun githubOAuth2SignIn(code: String): ResponseEntity<GithubUserInfo> {
        val githubAccessToken = githubOAuth2Client.getAccessToken(code) ?: throw CustomException(
            status = HttpStatus.UNAUTHORIZED,
            message = "너가 잘못했음"
        )
        println(githubAccessToken)
        val githubUserInfo = githubOAuth2Client.getUserInfo(
            githubAccessToken.accessToken ?: throw CustomException(
                status = HttpStatus.UNAUTHORIZED,
                message = "이거 또한 너가 잘못함"
            )
        )
        return githubUserInfo
    }
}