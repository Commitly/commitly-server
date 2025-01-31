package com.leegeonhee.commitly.domain.auth.domain.entity

import com.fasterxml.jackson.annotation.JsonManagedReference
import com.leegeonhee.commitly.domain.auth.domain.model.user.UserRole
import com.leegeonhee.commitly.domain.gpt.domain.entity.GptResponseEntity
import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
class UserEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null, // ID (PK)
    @Column(nullable = false, unique = true)
    val userId: Long,
    @Column(nullable = false)
    val login: String,
    @Column(nullable = false)
    var name: String, // Email,
    @Column(nullable = false)
    val role: UserRole = UserRole.ROLE_USER,
    @Column(nullable = false)
    val avataUrl: String,
    @OneToMany(mappedBy = "user", orphanRemoval = true)
    @JsonManagedReference
    val responses: List<GptResponseEntity> = mutableListOf(),
    @Column(nullable = true, updatable = false)
    var registrationDate: LocalDateTime? = null
){
    @PrePersist
    fun onPrePersist(){
        registrationDate = LocalDateTime.now()
    }
}