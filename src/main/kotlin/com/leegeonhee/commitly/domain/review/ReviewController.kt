package com.leegeonhee.commitly.domain.review

import com.leegeonhee.commitly.domain.review.model.ReviewRequestBody
import com.leegeonhee.commitly.gloabl.common.annotation.GetAuthenticatedId
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/review")
class ReviewController(private val reviewService: ReviewService) {
    @PostMapping("/create")
    fun writeReview(
        @GetAuthenticatedId id: Long,
        @RequestBody review: ReviewRequestBody
    ) = reviewService.writeReview(id, review)

    @GetMapping("/get")
    fun getAllReviews() = reviewService.getAllReviews()

    @DeleteMapping("/delete/{id}")
    fun deleteReview(
        @GetAuthenticatedId userId: Long,
        @PathVariable id: Long) = reviewService.deleteReview(userId,id)
}