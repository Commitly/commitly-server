package com.leegeonhee.commitly.gloabl.util

import com.leegeonhee.commitly.gloabl.exception.CustomException
import com.leegeonhee.commitly.gloabl.jwt.exception.CustomErrorCode
import io.github.bucket4j.Bandwidth
import io.github.bucket4j.Bucket
import io.github.bucket4j.Refill
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import java.time.Duration
import java.util.concurrent.ConcurrentHashMap

@Service
class RateLimitService(
){

    init {
        for (value in LimitStrategy.entries) {
            rateLimitMap[value.name] = Bucket.builder().addLimit(value.getBucket()).build()
        }
    }

    fun resolveBucket(name: String): Bucket {
        return rateLimitMap[name]?: throw CustomException(status = HttpStatus.NOT_FOUND, message = "그런 버킷 또 없습니다.")
    }

    companion object{
        private val rateLimitMap = ConcurrentHashMap<String, Bucket>()
    }

    // policy
    private enum class LimitStrategy{
        ROLE_USER {
            override fun getBucket(): Bandwidth {
                return Bandwidth.classic(3000, Refill.intervally(3000, Duration.ofMinutes(60)))
            }
        },
        ROLE_ADMIN{
            override fun getBucket(): Bandwidth {
                return Bandwidth.classic(10000, Refill.intervally(10000, Duration.ofMinutes(60)))
            }
        };

        abstract fun getBucket(): Bandwidth

        companion object {
            private val strategyMap = entries.associateBy(LimitStrategy::name)
            fun getStrategy(name: String) = strategyMap[name.uppercase()] ?: throw CustomException(status = HttpStatus.NOT_FOUND, message = "왜없지")
        }
    }
}