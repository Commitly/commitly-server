package com.leegeonhee.commitly.gloabl.jwt
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component

@Component
data class JwtProperties (

    @Value("\${jwt.secret}") val secretKey: String,

    @Value("\${jwt.expired.access}") val accessExpired: Long,

    @Value("\${jwt.expired.refresh}") val refreshExpired: Long

)