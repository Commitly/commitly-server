package com.leegeonhee.commitly.domain.user.domain.model.user

data class Plan(
    val collaborators: Int,
    val name: String,
    val private_repos: Int,
    val space: Int
)