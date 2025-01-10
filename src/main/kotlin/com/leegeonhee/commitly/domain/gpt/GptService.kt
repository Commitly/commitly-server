package com.leegeonhee.commitly.domain.gpt

import com.leegeonhee.commitly.domain.gpt.domain.dto.GptRequest
import com.leegeonhee.commitly.domain.gpt.domain.dto.GptResponse
import com.leegeonhee.commitly.domain.gpt.domain.dto.Message
import com.leegeonhee.commitly.domain.gpt.domain.dto.Role
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.client.WebClient
import reactor.core.publisher.Mono

@Service
class GptService(
    @Qualifier("openaiClient") private val gptClient: WebClient
) {
//    fun generateMemoir(commitMessages: String): Mono<GptResponse> {
//
//    }
}