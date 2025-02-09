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
    아래 커밋 메시지 목록을 참고하여, 오늘의 회고록을 5줄 내외로 작성해줘.
    이 회고록은 포트폴리오에 사용될 예정이므로 전문적이고 간결한 문체를 사용해야 해.
    
    반드시 다음 사항을 반영할 것:
    1. 리포지토리 이름은 반드시 영어 원문 그대로 기재.
    2. 오늘 수행한 주요 작업과 추가된 기능 또는 개선 사항을 명확히 언급할 것.
    3. 여러 커밋 메시지가 있는 경우, 핵심 내용을 통합하여 작성할 것.
    4. 날짜를 포함할 경우 "YYYY년 M월 D일" 형식으로 명시할 것.
    5. 커밋 활동을 통한 얻은 이로운 점도 기재할 것.
    
    커밋 메시지 목록:F
        ${message}
            """
        )
        val prompt = promptTemplate.create(mapOf("message" to message), requestModel)
        val result = openAiChatModel.call(prompt).result.output.content
        return result ?: "안왔음 ㅎㅎ"
    }


    fun getGptResponseFromDB(userId: Long, day: String) = gptResponseRepository.findByUserNameAndDay(userId, day)
}

