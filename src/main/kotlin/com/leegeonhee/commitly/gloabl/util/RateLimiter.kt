package com.leegeonhee.commitly.gloabl.util

import org.springframework.stereotype.Component
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.atomic.AtomicInteger
import java.util.concurrent.atomic.AtomicLong

@Component
class RateLimiter {
    private val requestCounts = ConcurrentHashMap<String, AtomicInteger>()
    private val lastResetTimes = ConcurrentHashMap<String, AtomicLong>()

    companion object {
        private const val REQUESTS_PER_MINUTE = 100
        private const val WINDOW_SIZE_MS = 60_000 // 1분
    }

    fun tryAcquire(clientId: String): Boolean {
        val now = System.currentTimeMillis()
        val lastResetTime = lastResetTimes.computeIfAbsent(clientId) { AtomicLong(now) }
        val count = requestCounts.computeIfAbsent(clientId) { AtomicInteger(0) }

        // 1분이 지났다면 카운터 리셋
        if (now - lastResetTime.get() >= WINDOW_SIZE_MS) {
            count.set(0)
            lastResetTime.set(now)
        }

        // 현재 윈도우의 요청 수가 제한을 초과하지 않았다면 증가
        return if (count.get() < REQUESTS_PER_MINUTE) {
            count.incrementAndGet()
            true
        } else {
            false
        }
    }
}