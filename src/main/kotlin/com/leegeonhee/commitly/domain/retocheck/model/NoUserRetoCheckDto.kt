package com.leegeonhee.commitly.domain.retocheck.model

import jakarta.persistence.Column
import org.springframework.data.annotation.CreatedDate
import java.time.LocalDate

data class NoUserRetoCheckDto(
    val retoDate: LocalDate,
    val createdAt: LocalDate,
)