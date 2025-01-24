package com.leegeonhee.commitly.domain.gpt.repository

import com.leegeonhee.commitly.domain.github.domain.entity.CommitInfoEntity
import com.leegeonhee.commitly.domain.gpt.domain.dto.GptResponse
import com.leegeonhee.commitly.domain.gpt.domain.entity.GptResponseEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param

interface GptResponseRepository: JpaRepository<GptResponseEntity, Long> {
    @Query("SELECT g FROM GptResponseEntity g WHERE g.user.id = :userId AND g.responseDate LIKE :day%")
    fun findByUserNameAndDay(@Param("userId") userId: Long, @Param("day") day: String): List<GptResponseEntity>
}