package com.leegeonhee.commitly.domain.github.domain.entity

import jakarta.persistence.*
import java.time.LocalDate

@Entity
@Table(name = "commit_info")
class CommitInfoEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @Column(name= "user_name",nullable = false)
    val userName: String,

    @Column(name = "repository_name", nullable = false)
    val repositoryName: String,

    @Column(name = "message", nullable = false, columnDefinition = "TEXT")
    val message: String,

    @Column(name = "committed_date", nullable = false)
    val committedDate: String
)
