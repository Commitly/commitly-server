package com.leegeonhee.commitly.domain.review

import com.leegeonhee.commitly.domain.review.entity.ReviewEntity
import org.springframework.data.jpa.repository.JpaRepository

interface ReviewRepository : JpaRepository<ReviewEntity, Long> {
}