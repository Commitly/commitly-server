package com.leegeonhee.commitly.domain.gpt.domain.entity

import com.leegeonhee.commitly.domain.auth.domain.entity.User
import jakarta.persistence.*

@Entity
@Table(name = "gpt_response")
class GptResponseEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @ManyToOne(fetch = FetchType.LAZY, cascade = [CascadeType.PERSIST]) // CascadeType 설정
    @JoinColumn(name = "user_id", nullable = false)
    val user: User,
    @Column(nullable = false)
    val response: String,

    @Column(nullable = false)
    val date: String,

)
