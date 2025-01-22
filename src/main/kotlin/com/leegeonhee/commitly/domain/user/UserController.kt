package com.leegeonhee.commitly.domain.user

import com.leegeonhee.commitly.domain.auth.domain.repository.UserRepository
import com.leegeonhee.commitly.gloabl.common.annotation.GetAuthenticatedId
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/user")
class UserController(
    private val userService: UserService
) {
//    @RateLimit
    @GetMapping("info")
    fun getUserInfo(
        @GetAuthenticatedId userId: Long,
    ) = userService.findByUserId(userId)
}