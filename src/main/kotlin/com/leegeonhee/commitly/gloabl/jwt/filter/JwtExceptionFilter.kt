package com.leegeonhee.commitly.gloabl.jwt.filter

import com.fasterxml.jackson.databind.ObjectMapper
import com.leegeonhee.commitly.gloabl.exception.CustomException
import com.leegeonhee.commitly.gloabl.exception.CustomExceptionTwo
import com.leegeonhee.commitly.gloabl.jwt.exception.CustomErrorCode
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter

@Component
class JwtExceptionFilter : OncePerRequestFilter() {
    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        try {
            filterChain.doFilter(request, response)
        } catch (exception: CustomExceptionTwo) {
            sendErrorResponse(response, exception)
        }
    }

    private fun sendErrorResponse(response: HttpServletResponse, exception: CustomExceptionTwo) {
        val error: CustomErrorCode = exception.customErrorCode

        response.status = error.status.value()
        response.contentType = "application/json"
        response.characterEncoding = "UTF-8"

        val mapper = ObjectMapper()
        val map = HashMap<String, Any>()

        map["message"] = error.message
        map["status"] = error.status.value()

        response.writer.write(mapper.writeValueAsString(map))
    }
}