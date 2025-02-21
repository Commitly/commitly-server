package com.leegeonhee.commitly.gloabl.jwt.filter

import com.fasterxml.jackson.databind.ObjectMapper
import com.leegeonhee.commitly.domain.auth.domain.model.user.UserRole
import com.leegeonhee.commitly.gloabl.common.BaseResponse
import com.leegeonhee.commitly.gloabl.exception.CustomException
import com.leegeonhee.commitly.gloabl.jwt.JwtUtils
import com.leegeonhee.commitly.gloabl.jwt.exception.JwtErrorCode
import com.leegeonhee.commitly.gloabl.jwt.exception.type.JwtErrorType
import com.leegeonhee.commitly.gloabl.util.RateLimitService
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.HttpStatus
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter



class JwtAuthenticationFilter(
    private val jwtUtils: JwtUtils,
    private val objectMapper: ObjectMapper,
    private val rateLimitService: RateLimitService,
) : OncePerRequestFilter() {

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        val token: String? = request.getHeader("Authorization")
        val path: String = request.servletPath



        if (path.startsWith("/swagger") || path.startsWith("/v3/api-docs")) {
            filterChain.doFilter(request, response)
            return
        }

        if (path.startsWith("/auth") || path.startsWith("/mail")) {
            filterChain.doFilter(request, response)
            return
        }

        if (path.startsWith("/login")) {
            filterChain.doFilter(request, response)
            return
        }

        if (token.isNullOrEmpty() || !token.startsWith("Bearer ")) {
            setErrorResponse(response, JwtErrorCode.JWT_EMPTY_EXCEPTION)
        } else {
            val userRole = jwtUtils.getUserRole(jwtUtils.getToken(token))
            val bucket = rateLimitService.resolveBucket(userRole)

            if (!bucket.tryConsume(1)) {
                response.status = HttpStatus.TOO_MANY_REQUESTS.value()
                return
            }
            when (jwtUtils.checkTokenInfo(jwtUtils.getToken(token))) {
                JwtErrorType.OK -> {
                    SecurityContextHolder.getContext().authentication = jwtUtils.getAuthentication(token)
                    doFilter(request, response, filterChain)
                }

                JwtErrorType.ExpiredJwtException -> setErrorResponse(response, JwtErrorCode.JWT_TOKEN_EXPIRED)
                JwtErrorType.SignatureException -> setErrorResponse(response, JwtErrorCode.JWT_TOKEN_SIGNATURE_ERROR)
                JwtErrorType.MalformedJwtException -> setErrorResponse(response, JwtErrorCode.JWT_TOKEN_ERROR)
                JwtErrorType.UnsupportedJwtException -> setErrorResponse(
                    response,
                    JwtErrorCode.JWT_TOKEN_UNSUPPORTED_ERROR
                )

                JwtErrorType.IllegalArgumentException -> setErrorResponse(
                    response,
                    JwtErrorCode.JWT_TOKEN_ILL_EXCEPTION
                )

                JwtErrorType.UNKNOWN_EXCEPTION -> setErrorResponse(response, JwtErrorCode.JWT_UNKNOWN_EXCEPTION)
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