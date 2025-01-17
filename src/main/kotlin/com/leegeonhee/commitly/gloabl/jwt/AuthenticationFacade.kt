package com.leegeonhee.commitly.gloabl.jwt

import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component

@Component
class AuthenticationFacade {
    fun getAuthenticatedUserId(): Long {
        val authentication = SecurityContextHolder.getContext().authentication
        if (authentication != null && authentication.principal is JwtUserDetails) {
            return (authentication.principal as JwtUserDetails).id ?:
            throw IllegalStateException("Authenticated user ID is null")
        }
        throw IllegalStateException("User is not authenticated")
    }
}