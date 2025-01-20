package com.leegeonhee.commitly.domain.auth.domain.internal.github

import com.leegeonhee.commitly.domain.auth.domain.model.GithubProperties
import com.leegeonhee.commitly.domain.auth.domain.model.OAuthAccessTokenRequest
import com.leegeonhee.commitly.domain.auth.domain.model.OAuthTokensResponse
import com.leegeonhee.commitly.domain.auth.domain.model.user.GithubUserInfo
import com.leegeonhee.commitly.domain.auth.domain.model.user.git.GithubCommitResponse
import com.leegeonhee.commitly.gloabl.exception.CustomException
import org.springframework.http.*
import org.springframework.stereotype.Component
import org.springframework.web.client.*

@Component
class GithubOAuth2Client(
    private val githubProperties: GithubProperties,
    private val restTemplate: RestTemplate
) {
    fun getAccessToken(code: String): OAuthTokensResponse? {
        val body = OAuthAccessTokenRequest(
            clientId = githubProperties.clientId?: throw CustomException(HttpStatus.UNAUTHORIZED, "ㄷㄷ"),
            clientSecret = githubProperties.clientSecret?:  throw CustomException(HttpStatus.UNAUTHORIZED, "ㄷㄷ"),
            code = code
        )
        val headers = HttpHeaders()
        headers.contentType = MediaType.APPLICATION_JSON
        val request = HttpEntity(body, headers)
        println(request)
        return  restTemplate.postForEntity(
            "https://github.com/login/oauth/access_token",request,OAuthTokensResponse::class.java
        ).body
    }

    fun getUserInfo(token: String): GithubUserInfo? {
        // 헤더 설정
        val headers = HttpHeaders()
        headers.set("Authorization", "Bearer $token")

        // HttpEntity 생성 (헤더만 포함)
        val entity = HttpEntity<String>(headers)

        // GET 요청 보내기
        val response = restTemplate.exchange(
            "https://api.github.com/user",
            HttpMethod.GET,
            entity,
            GithubUserInfo::class.java
        )
        return response.body
    }

    fun myGitLog(token: String,user: String): GithubCommitResponse? {
        // 헤더 설정
        val headers = HttpHeaders()
        headers.set("Authorization", "Bearer $token")

        // HttpEntity 생성 (헤더만 포함)
        val entity = HttpEntity<String>(headers)

        // GET 요청 보내기
        val response = restTemplate.exchange(
            "https://api.github.com/users/$user/events",
            HttpMethod.GET,
            entity,
            GithubCommitResponse::class.java
        )
        return response.body
    }
}