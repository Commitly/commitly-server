package com.leegeonhee.commitly.domain.user.domain.model.user.user

import com.leegeonhee.commitly.domain.user.domain.model.user.UserRole
import com.leegeonhee.commitly.domain.gpt.domain.entity.GptResponseEntity

data class User(
    val id: Long? = null, // ID (PK)
    val userId: Long,
    val login: String,
    var name: String, // Email,
    val role: UserRole = UserRole.ROLE_USER,
    val responses: List<GptResponseEntity> = mutableListOf()
)