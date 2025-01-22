package com.leegeonhee.commitly.domain.auth.presentation

import com.leegeonhee.commitly.domain.auth.domain.model.RefreshToken
import com.leegeonhee.commitly.domain.auth.service.AuthService
import org.springframework.web.bind.annotation.*


@RestController
@RequestMapping("/login")
class AuthController(
    private val authService: AuthService,
) {

//    @RateLimit
    @GetMapping("/oauth2/code/github")
    fun githubOAuth2SignIn(@RequestParam code: String) = authService.githubOAuth2SignIn(code)

//    @RateLimit
    @PostMapping("/refresh")
    fun refreshToken(@RequestBody token: RefreshToken) = authService.refreshToken(token)
}