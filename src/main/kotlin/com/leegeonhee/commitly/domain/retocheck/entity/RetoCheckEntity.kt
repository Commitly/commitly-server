package com.leegeonhee.commitly.domain.retocheck.entity

import com.leegeonhee.commitly.domain.auth.domain.entity.UserEntity
import jakarta.persistence.*
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.time.LocalDate
import java.time.LocalDateTime

@Entity
@EntityListeners(AuditingEntityListener::class)
class RetoCheckEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null, // ID (PK)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    val author: UserEntity,
    @Column(name = "reto_date", nullable = false)
    val retoDate: LocalDate,
    @CreatedDate
    val createdAt: LocalDateTime = LocalDateTime.now(),
)