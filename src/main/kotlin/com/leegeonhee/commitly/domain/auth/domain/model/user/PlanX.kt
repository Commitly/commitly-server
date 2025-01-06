package com.leegeonhee.commitly.domain.auth.domain.model.user

data class PlanX(
    val collaborators: Int,
    val name: String,
    val private_repos: Int,
    val space: Int
)