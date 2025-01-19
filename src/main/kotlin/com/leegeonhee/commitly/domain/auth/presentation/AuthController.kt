package com.leegeonhee.commitly.domain.user.presentation

import com.leegeonhee.commitly.domain.user.service.AuthService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController


@RestController
class AuthController(
    private val authService: AuthService,
) {

    @GetMapping("/login/oauth2/code/github")
    fun githubOAuth2SignIn(@RequestParam code: String) = authService.githubOAuth2SignIn(code)

}