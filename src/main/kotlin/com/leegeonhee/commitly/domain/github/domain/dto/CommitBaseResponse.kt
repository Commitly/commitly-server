package com.leegeonhee.commitly.domain.github.domain.dto

import com.fasterxml.jackson.annotation.JsonInclude
import org.springframework.http.HttpStatus

@JsonInclude(JsonInclude.Include.NON_NULL)
data class CommitBaseResponse<T>(

    val status: Int = HttpStatus.OK.value(),
    val state: String? = "OK",
    val message: String,
    val tag: Set<String>,
    val data: T? = null

)