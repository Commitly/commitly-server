package com.leegeonhee.commitly.gloabl.jwt

import com.fasterxml.jackson.databind.ObjectMapper
import com.leegeonhee.commitly.gloabl.common.BaseResponse
import com.leegeonhee.commitly.gloabl.jwt.exception.JwtErrorCode
import com.leegeonhee.commitly.gloabl.jwt.exception.type.JwtErrorType
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.filter.OncePerRequestFilter
class JwtAuthenticationFilter(
    private val jwtUtils: JwtUtils,
    private val objectMapper: ObjectMapper
) : OncePerRequestFilter() {

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        val token: String? = request.getHeader("Authorization")
        val path: String = request.servletPath

        if (path.startsWith("/login/")
        ) {
            filterChain.doFilter(request, response)
            return
        }
        if (path.startsWith("/swagger-ui/index.html#/")
        ) {
            filterChain.doFilter(request, response)
            return
        }

        if (token.isNullOrEmpty() || !token.startsWith("Bearer ")) {
            println("Token is empty or does not start with Bearer.")
            setErrorResponse(response, JwtErrorCode.JWT_EMPTY_EXCEPTION)
        } else {
            println("Token received: $token")
            try {
                when (jwtUtils.checkTokenInfo(jwtUtils.getToken(token))) {
                    JwtErrorType.OK -> {
                        // 인증된 사용자를 SecurityContext에 설정
                        val authentication = jwtUtils.getAuthentication(token)
                        SecurityContextHolder.getContext().authentication = authentication
                        println("SecurityContextHolder authentication after setting: ${SecurityContextHolder.getContext().authentication}")

                        doFilter(request, response, filterChain)
                        println("Authentication successful: $authentication")

                    }

                    JwtErrorType.ExpiredJwtException -> {
                        println("Token has expired.")
                        setErrorResponse(response, JwtErrorCode.JWT_TOKEN_EXPIRED)
                    }
                    JwtErrorType.SignatureException -> {
                        println("Token has signature error.")
                        setErrorResponse(response, JwtErrorCode.JWT_TOKEN_SIGNATURE_ERROR)
                    }
                    JwtErrorType.MalformedJwtException -> {
                        println("Token is malformed.")
                        setErrorResponse(response, JwtErrorCode.JWT_TOKEN_ERROR)
                    }
                    JwtErrorType.UnsupportedJwtException -> {
                        println("Token is unsupported.")
                        setErrorResponse(response, JwtErrorCode.JWT_TOKEN_UNSUPPORTED_ERROR)
                    }
                    JwtErrorType.IllegalArgumentException -> {
                        println("Token argument is invalid.")
                        setErrorResponse(response, JwtErrorCode.JWT_TOKEN_ILL_EXCEPTION)
                    }
                    JwtErrorType.UNKNOWN_EXCEPTION -> {
                        println("Unknown token error.")
                        setErrorResponse(response, JwtErrorCode.JWT_UNKNOWN_EXCEPTION)
                    }
                }
            } catch (e: Exception) {
                println("Exception occurred during token verification: ${e.message}")
                setErrorResponse(response, JwtErrorCode.JWT_UNKNOWN_EXCEPTION)
            }
        }

    }

    private fun setErrorResponse(
        response: HttpServletResponse,
        errorCode: JwtErrorCode
    ) {
        response.status = errorCode.status.value()
        response.contentType = "application/json;charset=UTF-8"

        response.writer.write(
            objectMapper.writeValueAsString(
                BaseResponse<String>(
                    status = errorCode.status.value(),
                    state = errorCode.state,
                    message = errorCode.message
                )
            )
        )
    }
}