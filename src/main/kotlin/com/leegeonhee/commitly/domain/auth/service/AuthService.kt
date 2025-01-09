package com.leegeonhee.commitly.domain.auth.service

import com.leegeonhee.commitly.domain.auth.domain.entity.User
import com.leegeonhee.commitly.domain.auth.domain.model.OAuthTokensResponse
import com.leegeonhee.commitly.domain.auth.domain.model.user.GithubUserInfo
import com.leegeonhee.commitly.domain.auth.domain.model.user.git.GithubCommitResponse
import org.springframework.http.ResponseEntity


interface AuthService {
    fun githubOAuth2SignIn(code: String): GithubCommitResponse?
}