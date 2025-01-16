package com.leegeonhee.commitly.domain.github

import CommitInfo
import com.leegeonhee.commitly.gloabl.common.BaseResponse
import com.leegeonhee.commitly.gloabl.common.annotation.GetAuthenticatedId
import org.springframework.format.annotation.DateTimeFormat
import org.springframework.web.bind.annotation.*
import java.time.LocalDate

@RestController
@RequestMapping("/github")
class GitHubController(private val gitHubService: GitHubService) {

    @GetMapping("/commits/messages")
    suspend fun getCommitMessages(
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
    suspend fun getMakeCommitMessages(
        @GetAuthenticatedId userId: Long,
        @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") date: String
    ) = gitHubService.generateMemoirWithGpt(
        userId = userId,
        date = LocalDate.parse(date)
    )
}