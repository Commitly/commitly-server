package com.leegeonhee.commitly.domain.gpt

import com.leegeonhee.commitly.domain.gpt.domain.dto.GptRequest
import com.leegeonhee.commitly.domain.gpt.domain.dto.GptResponse
import com.leegeonhee.commitly.domain.gpt.domain.dto.Message
import com.leegeonhee.commitly.domain.gpt.domain.dto.Role
import com.leegeonhee.commitly.domain.gpt.domain.entity.GptResponseEntity
import com.leegeonhee.commitly.domain.gpt.repository.GptResponseRepository
import org.springframework.ai.chat.prompt.PromptTemplate
import org.springframework.ai.openai.OpenAiChatModel
import org.springframework.ai.openai.OpenAiChatOptions
import org.springframework.ai.openai.api.OpenAiApi
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.client.WebClient
import reactor.core.publisher.Mono

@Service
class GptService(
    private val openAiChatModel: OpenAiChatModel,
    private val gptResponseRepository: GptResponseRepository
) {
    private val requestModel = OpenAiChatOptions.builder().withModel(OpenAiApi.ChatModel.GPT_4_O_MINI).build()

    fun askToGptRequest(message: String): String {
        val promptTemplate = PromptTemplate(
            """
    너는 아래 커밋 메시지 리스트를 보고 3줄 이내로 끝나는 이날의 회고록을 적어야해. 
    또한 회고록에서는 리포지토리 이름을 내서 어떤 프로젝트에서 이것을 했다 식으로 말을해,
    리포지토리 이름은 잘못말할 수 있으니 영어이름 그대로 말을해.
    이것은 포트폴리오에 적기 위함이므로 포트폴리오 느낌나게 적어줘
    커밋 메시지 목록: 
        ${message}
"""
        )
        val prompt = promptTemplate.create(mapOf("message" to message), requestModel)
        val result = openAiChatModel.call(prompt).result.output.content
        return result ?: "안왔음 ㅎㅎ"
    }
}

