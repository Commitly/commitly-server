package com.leegeonhee.commitly.domain.user

import com.leegeonhee.commitly.domain.auth.domain.entity.UserEntity
import com.leegeonhee.commitly.domain.auth.domain.repository.UserRepository
import com.leegeonhee.commitly.gloabl.common.BaseResponse
import org.springframework.stereotype.Service


@Service
class UserService(
    private val userRepository: UserRepository
) {
    fun findByUserId(userId: Long): BaseResponse<UserEntity> {
        val user = userRepository.findById(userId)
        return if (user.isPresent) {
            BaseResponse(
                status = 200,
                message = "조회완료",
                data = user.get()
            )
        } else {
            BaseResponse(
                status = 404,
                message = "그런 사람 또 없습니다."
            )
        }
    }
}
