package com.leegeonhee.commitly.domain.auth.presentation

import com.leegeonhee.commitly.domain.auth.domain.model.OAuthTokensResponse
import com.leegeonhee.commitly.domain.auth.domain.model.user.GithubUserInfo
import com.leegeonhee.commitly.domain.auth.service.AuthService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController


@RestController
class AuthController(
    private val authService: AuthService,
) {

    @GetMapping("/login/oauth2/code/github")
    fun githubOAuth2SignIn(@RequestParam code: String): ResponseEntity<GithubUserInfo> {
        return authService.githubOAuth2SignIn(code)
    }


}