package com.leegeonhee.commitly.domain.auth.domain.model.user

data class Plan(
    val collaborators: Int,
    val name: String,
    val private_repos: Int,
    val space: Int
)