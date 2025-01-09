package com.leegeonhee.commitly.domain.auth.domain.repository

import com.leegeonhee.commitly.domain.auth.domain.entity.User
import org.springframework.data.jpa.repository.JpaRepository

interface UserRepository : JpaRepository<User, Long> {
    fun findByUserId(userId:Long):List<User>
}