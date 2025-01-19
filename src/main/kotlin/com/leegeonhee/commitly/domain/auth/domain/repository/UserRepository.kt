package com.leegeonhee.commitly.domain.user.domain.repository

import com.leegeonhee.commitly.domain.user.domain.entity.UserEntity
import org.springframework.data.jpa.repository.JpaRepository

interface UserRepository : JpaRepository<UserEntity, Long> {
    fun findAllByUserId(userId:Long):List<UserEntity>
    fun findByUserId(userId:Long):UserEntity?
    fun findByLogin(username:String):UserEntity?
}