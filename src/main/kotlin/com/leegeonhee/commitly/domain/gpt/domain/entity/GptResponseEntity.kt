package com.leegeonhee.commitly.domain.gpt.domain.entity

import jakarta.persistence.*

@Entity
@Table(name = "gpt_response")
class GptResponseEntity (
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,
    @Column(nullable = false)
    val user: String, //TODO 나중에 USER ENTITY 만든다음에 외래키로 박아야함
    @Column(nullable = false)
    val response: String
)