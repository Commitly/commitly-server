package com.leegeonhee.commitly.domain.github

import CommitInfo
import com.leegeonhee.commitly.gloabl.common.BaseResponse
import com.leegeonhee.commitly.gloabl.common.annotation.GetAuthenticatedId
import com.leegeonhee.commitly.gloabl.util.RateLimitService
import org.springframework.format.annotation.DateTimeFormat
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.time.LocalDate

@RestController
@RequestMapping("/github")
class GitHubController(
    private val gitHubService: GitHubService,
    private val rateLimitService: RateLimitService
) {

    //    @RateLimit
    @GetMapping("/commits/messages")
    fun getCommitMessages(
        @GetAuthenticatedId userId: Long,
        @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") date: String
    ): BaseResponse<List<CommitInfo>> {
        val targetDate = LocalDate.parse(date)
        return gitHubService.getCommitMessages(userId, targetDate)
    }

    //    @RateLimit
    @GetMapping("/commits/{userName}/fromDB")
    fun getFromDB(
        @PathVariable userName: String,
        @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") date: String
    ) = gitHubService.getFromDB(userName, date)


    @GetMapping("/gpt/get")
    fun getGptFromDB(
        @GetAuthenticatedId userId: Long,
        @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") date: String
    ) = gitHubService.getGptResponseFromDb(userId, date)
//
//

    //    @RateLimit
    @GetMapping("/gpt/make")
    fun getMakeCommitMessages(
        @GetAuthenticatedId userId: Long,
        @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") date: String
    ): ResponseEntity<BaseResponse<String>> {
        if (!rateLimitService.isRequestAllowed(userId)) {

            return ResponseEntity.status(429).body(BaseResponse(
                status = 401,
                message = "당신은 무료 요금제라서 하루에 다섯번 밖에 못함",
            ))

        }
        val response = gitHubService.generateMemoirWithGpt(userId, LocalDate.parse(date))
        return ResponseEntity.ok(response)
    }

}