package com.leegeonhee.commitly.domain.auth.domain.model.user.user

import com.leegeonhee.commitly.domain.auth.domain.model.user.UserRole
import com.leegeonhee.commitly.domain.gpt.domain.entity.GptResponseEntity

data class NoResponseUser(
    val id: Long? = null, // ID (PK)
    val userId: Long,
    val login: String,
    var name: String, // Email,
    val role: UserRole = UserRole.ROLE_USER,
    val avataUrl: String,
)
