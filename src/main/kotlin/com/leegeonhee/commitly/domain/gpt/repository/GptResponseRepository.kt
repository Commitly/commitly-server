package com.leegeonhee.commitly.domain.gpt.repository

import com.leegeonhee.commitly.domain.gpt.domain.dto.GptResponse
import com.leegeonhee.commitly.domain.gpt.domain.entity.GptResponseEntity
import org.springframework.data.jpa.repository.JpaRepository

interface GptResponseRepository: JpaRepository<GptResponseEntity, Long> {

}