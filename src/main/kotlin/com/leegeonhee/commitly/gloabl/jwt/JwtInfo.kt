package com.leegeonhee.commitly.gloabl.jwt

data class JwtInfo(
    val accessToken: String,
    val refreshToken: String
)