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

    fun saveRetoDate(user: Long, date: String): RetoCheckEntity {
        val formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME // "yyyy-MM-dd'T'HH:mm:ss" 형식
        val localDateTime = LocalDate.parse(date, formatter)


        return retoCheckRepository.save(
            RetoCheckEntity(
                author = userRepository.findByIdOrNull(user)!!,
                retoDate = localDateTime,
            )
        )
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