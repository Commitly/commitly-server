package com.leegeonhee.commitly.domain.user.domain.model.user.git

data class Commit(
    val author: Author?,
    val distinct: Boolean?,
    val message: String?,
    val sha: String?,
    val url: String?
)