package com.leegeonhee.commitly.domain.github

import CommitInfo
import com.leegeonhee.commitly.domain.github.domain.dto.CommitBaseResponse
import com.leegeonhee.commitly.gloabl.common.BaseResponse
import com.leegeonhee.commitly.gloabl.common.annotation.GetAuthenticatedId
//import com.leegeonhee.commitly.gloabl.util.RateLimitService
import org.springframework.format.annotation.DateTimeFormat
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.time.LocalDate

@RestController
@RequestMapping("/github")
class GitHubController(
    private val gitHubService: GitHubService,
//    private val rateLimitService: RateLimitService
) {

    //    @RateLimit
    @GetMapping("/commits/messages")
    fun getCommitMessages(
        @GetAuthenticatedId userId: Long,
        @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") date: String
    ): CommitBaseResponse<List<CommitInfo>> {
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
        @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") date: String,
        @RequestParam(
            value = "repositoryName",
            defaultValue = "ALL"
        ) repositoryName: String
    ): ResponseEntity<BaseResponse<String>> {
        val response = gitHubService.generateMemoirWithGpt(userId, LocalDate.parse(date),repositoryName)
        return ResponseEntity.ok(response)
    }

}