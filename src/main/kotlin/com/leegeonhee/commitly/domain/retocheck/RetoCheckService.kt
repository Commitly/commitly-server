package com.leegeonhee.commitly.domain.retocheck

import com.leegeonhee.commitly.domain.auth.domain.entity.UserEntity
import com.leegeonhee.commitly.domain.auth.domain.repository.UserRepository
import com.leegeonhee.commitly.domain.retocheck.entity.RetoCheckEntity
import com.leegeonhee.commitly.domain.review.model.ReviewRequestBody
import com.leegeonhee.commitly.gloabl.common.BaseResponse
import com.leegeonhee.commitly.gloabl.exception.CustomException
import org.springframework.data.repository.findByIdOrNull
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import java.time.LocalDate
import java.time.LocalDateTime

@Service
class RetoCheckService(
    private val retoCheckRepository: RetoCheckRepository,
    private val userRepository: UserRepository,
) {

    fun saveRetoDate(user: Long, date: LocalDate) =
        retoCheckRepository.save(
            RetoCheckEntity(
                author = userRepository.findByIdOrNull(user)!!,
                retoDate = date,
            )
        )

    fun getMyDate(id: Long): List<RetoCheckEntity> =
        retoCheckRepository.getAllRetoCheckEntityByAuthorOrIdNull(
            userRepository.findByIdOrNull(id) ?: throw CustomException(HttpStatus.NOT_FOUND, "없다는 뜻")
        ) ?: throw CustomException(
            status = HttpStatus.NOT_FOUND,
            message = "404 Not Found 라는 뜻"
        )


}