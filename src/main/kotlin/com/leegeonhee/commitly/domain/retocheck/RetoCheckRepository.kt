package com.leegeonhee.commitly.domain.retocheck

import com.leegeonhee.commitly.domain.auth.domain.entity.UserEntity
import com.leegeonhee.commitly.domain.retocheck.entity.RetoCheckEntity
import com.leegeonhee.commitly.domain.review.entity.ReviewEntity
import org.springframework.data.jpa.repository.JpaRepository

interface RetoCheckRepository : JpaRepository<RetoCheckEntity, Long> {
    fun getRetoCheckEntityByAuthorOrIdNull(author: UserEntity): RetoCheckEntity?
}