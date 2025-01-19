package com.leegeonhee.commitly.domain.auth.domain.mapper

import com.leegeonhee.commitly.domain.auth.domain.entity.UserEntity
import com.leegeonhee.commitly.domain.auth.domain.model.user.user.User
import org.springframework.stereotype.Component

@Component
class UserMapper(
) {
    fun toDomain(entity: UserEntity): User {
        return User(
            id = entity.id,
            userId = entity.userId,
            login = entity.login,
            name = entity.name,
            role = entity.role,
            responses = entity.responses
        )
    }
}