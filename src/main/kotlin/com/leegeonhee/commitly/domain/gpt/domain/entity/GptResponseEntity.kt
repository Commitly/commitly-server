package com.leegeonhee.commitly.domain.gpt.domain.entity

import com.fasterxml.jackson.annotation.JsonBackReference
import com.leegeonhee.commitly.domain.auth.domain.entity.UserEntity
import jakarta.persistence.*
import org.springframework.data.annotation.CreatedDate
import java.time.LocalDateTime

@Entity
@Table(name = "gpt_response")
class GptResponseEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @ManyToOne(fetch = FetchType.LAZY) // CascadeType 설정
    @JoinColumn(name = "user_id", nullable = false)
    @JsonBackReference
    val user: UserEntity,
    @Column(nullable = false)
    val response: String,

    @Column(nullable = false)
    val responseDate: String,

    @Column(nullable = true)
    var registrationDate: LocalDateTime? = null
){
}
