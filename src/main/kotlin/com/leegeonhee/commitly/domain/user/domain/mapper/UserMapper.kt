package com.leegeonhee.commitly.domain.user.domain.mapper

import com.leegeonhee.commitly.domain.user.domain.entity.UserEntity
import com.leegeonhee.commitly.domain.user.domain.model.user.user.User
import org.apache.catalina.mapper.Mapper
import org.springframework.stereotype.Component

@Component
class UserMapper(
){
    fun toDomain(entity: UserEntity): User {
        return User(
            userId = entity.userId,
            login = entity.login,
            name = entity.name,
            role = entity.role,
            responses = entity.responses
        )
    }
}