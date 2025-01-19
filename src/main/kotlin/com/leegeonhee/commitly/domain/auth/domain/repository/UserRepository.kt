package com.leegeonhee.commitly.domain.auth.domain.repository

import com.leegeonhee.commitly.domain.auth.domain.entity.UserEntity
import org.springframework.data.jpa.repository.JpaRepository

interface UserRepository : JpaRepository<UserEntity, Long> {
    fun findAllByUserId(userId:Long):List<UserEntity>
    fun findByUserId(userId:Long):UserEntity?
    fun findByLogin(username:String):UserEntity?
}