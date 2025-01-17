package com.leegeonhee.commitly.domain.github

import CommitInfo
import com.leegeonhee.commitly.gloabl.common.BaseResponse
import com.leegeonhee.commitly.gloabl.common.annotation.GetAuthenticatedId
import org.springframework.format.annotation.DateTimeFormat
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.time.LocalDate

@RestController
@RequestMapping("/github")
class GitHubController(private val gitHubService: GitHubService) {

    @GetMapping("/commits/messages")
    fun getCommitMessages(
        @GetAuthenticatedId userId: Long,
        @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") date: String
    ): BaseResponse<List<CommitInfo>> {
        val targetDate = LocalDate.parse(date)
        return gitHubService.getCommitMessages(userId, targetDate)
    }

    @GetMapping("/commits/{userName}/fromDB")
    fun getFromDB(
        @PathVariable userName: String,
        @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") date: String
    )  = gitHubService.getFromDB(userName, date)

//
//

    @GetMapping("/make")
    fun getMakeCommitMessages(
        @GetAuthenticatedId userId: Long,
        @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") date: String
    ): ResponseEntity<BaseResponse<String>> {
        val response = gitHubService.generateMemoirWithGpt(userId, LocalDate.parse(date))
        return ResponseEntity.ok(response)
    }

}