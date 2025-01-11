package com.leegeonhee.commitly.domain.github

import CommitInfo
import com.leegeonhee.commitly.gloabl.common.BaseResponse
import org.springframework.format.annotation.DateTimeFormat
import org.springframework.web.bind.annotation.*
import java.time.LocalDate

@RestController
@RequestMapping("/github")
class GitHubController(private val gitHubService: GitHubService) {

    @GetMapping("/commits/{username}/messages")
    suspend fun getCommitMessages(
        @PathVariable username: String,
        @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") date: String
    ): BaseResponse<List<CommitInfo>> {
        val targetDate = LocalDate.parse(date)
        return gitHubService.getCommitMessages(username, targetDate)
    }

    @GetMapping("/commits/{userName}/fromDB")
    fun getFromDB(
        @PathVariable userName: String,
        @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") date: String
    )  = gitHubService.getFromDB(userName, date)

//
//

    @GetMapping("/make/{username}")
    suspend fun getMakeCommitMessages(
        @PathVariable username: String,
        @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") date: String
    ) = gitHubService.generateMemoirWithGpt(
        name = username,
        date = LocalDate.parse(date)
    )
}