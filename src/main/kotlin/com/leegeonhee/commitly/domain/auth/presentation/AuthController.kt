package com.leegeonhee.commitly.domain.auth.presentation

import com.leegeonhee.commitly.domain.auth.domain.model.GithubProperties
import com.leegeonhee.commitly.domain.auth.domain.model.OAuthTokensResponse
import com.leegeonhee.commitly.domain.auth.service.AuthService
import org.springframework.beans.factory.annotation.Value
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.client.RestTemplate


@RestController
class AuthController(
    private val authService: AuthService,
) {

    @GetMapping("/login/oauth2/code/github")
    fun getAccessToken(@RequestParam code: String): OAuthTokensResponse {
        return authService.getAccessToken(code)
    }


}