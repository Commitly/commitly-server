package com.leegeonhee.commitly.domain.user.domain.model.user

data class PlanX(
    val collaborators: Int,
    val name: String,
    val private_repos: Int,
    val space: Int
)