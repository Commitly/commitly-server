package com.leegeonhee.commitly.gloabl.util

import org.aspectj.lang.ProceedingJoinPoint
import org.aspectj.lang.annotation.Around
import org.aspectj.lang.annotation.Aspect
import org.aspectj.lang.annotation.Pointcut
import org.springframework.ai.chat.metadata.RateLimit
import org.springframework.stereotype.Component
import org.springframework.web.context.request.RequestContextHolder
import org.springframework.web.context.request.ServletRequestAttributes

@Aspect
@Component
class RateLimitAspect(
    private val rateLimiter: RateLimiter
) {
    @Pointcut("@annotation(rateLimit)")
    fun rateLimitPointcut(rateLimit: RateLimit) {}

    @Around("rateLimitPointcut(rateLimit)")
    fun enforceRateLimit(joinPoint: ProceedingJoinPoint): Any {
        val request = (RequestContextHolder.currentRequestAttributes() as ServletRequestAttributes).request
        val clientId = request.getHeader("X-Client-ID") ?: request.remoteAddr

        if (!rateLimiter.tryAcquire(clientId)) {
            throw RateLimitExceededException("Rate limit exceeded for client: $clientId")
        }

        return joinPoint.proceed()
    }
}