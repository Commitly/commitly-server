package com.leegeonhee.commitly.domain.retocheck

import com.leegeonhee.commitly.domain.review.model.ReviewRequestBody
import com.leegeonhee.commitly.gloabl.common.annotation.GetAuthenticatedId
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/reto")
class RetoCheckController(private val retoCheckService: RetoCheckService) {
    @PostMapping("/create")
    fun writeRetoCheck(
        @GetAuthenticatedId id: Long,
        @RequestBody review: ReviewRequestBody
    ) = retoCheckService.writeReview(id, review)

    @GetMapping("/get")
    fun getMyAllCheck(
        @GetAuthenticatedId id: Long
    ) = retoCheckService.getMyAllReto(id)



    @DeleteMapping("/delete/{id}")
    fun deleteCheck(
        @GetAuthenticatedId userId: Long,
        @PathVariable id: Long) = retoCheckService.deleteReview(userId,id)
}