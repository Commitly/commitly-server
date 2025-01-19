package com.leegeonhee.commitly.domain.auth.domain.model.user.git

data class GithubCommitResponseItem(
    val actor: Actor?,
    val created_at: String?,
    val id: String?,
    val org: Org?,
    val payload: Payload?,
    val `public`: Boolean?,
    val repo: Repo?,
    val type: String?
)