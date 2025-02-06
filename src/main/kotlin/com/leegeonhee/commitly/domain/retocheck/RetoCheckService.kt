package com.leegeonhee.commitly.domain.retocheck

import com.leegeonhee.commitly.domain.auth.domain.repository.UserRepository
import com.leegeonhee.commitly.domain.retocheck.entity.RetoCheckEntity
import com.leegeonhee.commitly.domain.retocheck.model.ItIsRetoCheckThatTheKotlinModernStyleOfValueClassIWantUseThisClassAOneTimeGood
import com.leegeonhee.commitly.gloabl.exception.CustomException
import org.springframework.data.repository.findByIdOrNull
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@Service
class RetoCheckService(
    private val retoCheckRepository: RetoCheckRepository,
    private val userRepository: UserRepository,
) {

    fun saveRetoDate(userId: Long, date: String): RetoCheckEntity {
        val formatter = DateTimeFormatter.ISO_LOCAL_DATE // "yyyy-MM-dd" 형식
        val localDate = LocalDate.parse(date, formatter)

        val user = userRepository.findByIdOrNull(userId) ?: throw CustomException(HttpStatus.NOT_FOUND, "사용자 없음")

        val exists = retoCheckRepository.existsByAuthorAndRetoDate(user, localDate)
        if (exists) {
            throw CustomException(HttpStatus.CONFLICT, "이미 해당 날짜에 기록이 있습니다.")
        }

        return retoCheckRepository.save(RetoCheckEntity(author = user, retoDate = localDate))
    }


    fun getMyDate(id: Long): List<ItIsRetoCheckThatTheKotlinModernStyleOfValueClassIWantUseThisClassAOneTimeGood> =
        retoCheckRepository.getAllRetoCheckEntityByAuthorOrIdNull(
            userRepository.findByIdOrNull(id) ?: throw CustomException(HttpStatus.NOT_FOUND, "없다는 뜻")
        )?.map {
            ItIsRetoCheckThatTheKotlinModernStyleOfValueClassIWantUseThisClassAOneTimeGood(
                retoDate = it.retoDate,
            )
        } ?: throw CustomException(
            status = HttpStatus.NOT_FOUND,
            message = "404 Not Found 라는 뜻"
        )


}