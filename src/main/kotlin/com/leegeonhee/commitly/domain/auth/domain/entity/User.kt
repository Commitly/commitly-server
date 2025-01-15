package com.leegeonhee.commitly.domain.auth.domain.entity

import com.leegeonhee.commitly.domain.gpt.domain.entity.GptResponseEntity
import jakarta.persistence.*

@Entity
class User(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null, // ID (PK)

    @Column(nullable = false)
    val userId: Long,
    @Column(nullable = false)
    val login: String,
    @Column(nullable = false)
    var name: String, // Email,
    @OneToMany(mappedBy = "user", cascade = [CascadeType.ALL], orphanRemoval = true)
    val responses: List<GptResponseEntity> = mutableListOf()
)