package com.leegeonhee.commitly.domain.auth.domain.repository

import com.leegeonhee.commitly.domain.auth.domain.entity.UserEntity
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface UserRepository : JpaRepository<UserEntity, Long> {
    override fun findById(id: Long): Optional<UserEntity>
    fun findByUserId(userId:Long):List<UserEntity>
    fun findByLogin(username:String):UserEntity
}