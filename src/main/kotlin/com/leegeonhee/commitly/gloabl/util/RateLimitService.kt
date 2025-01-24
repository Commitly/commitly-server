package com.leegeonhee.commitly.gloabl.util
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service // Spring이 이 클래스를 Bean으로 관리하게 해주는 어노테이션
class RateLimitService {

    private val userRequests: MutableMap<Long, MutableList<LocalDateTime>> = mutableMapOf()
    private val maxRequestsPerDay = 5

    fun isRequestAllowed(userId: Long): Boolean {
        val currentTime = LocalDateTime.now()
        val requests = userRequests.getOrDefault(userId, mutableListOf())

        // 24시간 전에 요청을 모두 지우기
        val cutoffTime = currentTime.minusHours(24)
        val recentRequests = requests.filter { it.isAfter(cutoffTime) }.toMutableList()

        if (recentRequests.size < maxRequestsPerDay) {
            // 요청 추가
            recentRequests.add(currentTime)
            userRequests[userId] = recentRequests
            return true
        } else {
            return false
        }
    }
}
