package com.leegeonhee.commitly.domain.review

import com.leegeonhee.commitly.domain.auth.domain.repository.UserRepository
import com.leegeonhee.commitly.domain.review.entity.ReviewEntity
import com.leegeonhee.commitly.domain.review.model.ReviewRequestBody
import com.leegeonhee.commitly.gloabl.common.BaseResponse
import com.leegeonhee.commitly.gloabl.exception.CustomException
import org.springframework.data.repository.findByIdOrNull
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service

@Service
class ReviewService(
    private val reviewRepository: ReviewRepository,
    private val userRepository: UserRepository
) {

    //    C
    fun writeReview(userId: Long, review: ReviewRequestBody): BaseResponse<ReviewEntity> {
        val user = getUser(userId)
        return BaseResponse(
            status = 200,
            message = "굿",
            data = reviewRepository.save(
                ReviewEntity(
                    author = user,
                    message = review.retoDate,
                )
            )
        )
    }

    //    R
    fun getAllReviews(): BaseResponse<MutableList<ReviewEntity>> =
        BaseResponse(
            status = 200,
            message = "에러가 안뜰거같은 기분",
            data = reviewRepository.findAll()
        )

    //    D
    fun deleteReview(userId: Long, reviewId: Long): BaseResponse<ReviewEntity> {
        val user = getUser(userId)
        val review = reviewRepository.findByIdOrNull(reviewId)?: throw CustomException(status = HttpStatus.NOT_FOUND, message = "없")
        if (review.author == user){
            reviewRepository.deleteById(reviewId)
            return BaseResponse(
                status = 201,
                message = "굿",
            )
        } else{
            return BaseResponse(
                status = 404,
                message = "NOT FOUND 라는 뜻"
            )
        }
    }


    private fun getUser(userId: Long) = userRepository.findByIdOrNull(userId)?: throw CustomException(status = HttpStatus.NOT_FOUND, message = "그런 사람 또 없습니다.")
}