package com.leegeonhee.commitly.domain.github

import CommitInfo
import org.springframework.format.annotation.DateTimeFormat
import org.springframework.web.bind.annotation.*
import java.time.LocalDate

@RestController
@RequestMapping("/api/github")
class GitHubController(private val gitHubService: GitHubService) {

    @GetMapping("/commits/{username}/messages")
    suspend fun getCommitMessages(
        @PathVariable username: String,
        @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") date: String
    ): List<CommitInfo> {
        val targetDate = LocalDate.parse(date)
        return gitHubService.getCommitMessages(username, targetDate)
    }
    @GetMapping("/commits/{username}/{organizations}/messages")
    suspend fun getOrganizationsCommitMessages(
        @PathVariable username: String,
        @PathVariable organizations: String,
        @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") date: String
    ): List<CommitInfo> {
        val targetDate = LocalDate.parse(date)
        return gitHubService.getOrganizationCommitMessages(username, targetDate,organizations)
    }
}