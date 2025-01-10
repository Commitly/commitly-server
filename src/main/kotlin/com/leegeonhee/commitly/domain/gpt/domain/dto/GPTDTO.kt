package com.leegeonhee.commitly.domain.gpt.domain.dto

data class GptRequest(
    val model: String = "gpt-4", // 모델 이름
    val messages: List<Message>,
    val temperature: Double = 0.7, // 텍스트의 창의성 수준 (0~1)
    val maxTokens: Int = 1000, // 생성할 최대 토큰 수
    val topP: Double? = null, // 확률 기반 필터링, 기본 null
    val frequencyPenalty: Double? = null, // 동일한 단어 반복을 줄이는 값
    val presencePenalty: Double? = null // 새로운 주제에 대한 응답 가능성 제어
)

data class Message(
    val role: Role, // "user", "system", "assistant"
    val content: String
)

enum class Role {
    USER, SYSTEM, ASSISTANT;

    override fun toString(): String {
        return name.lowercase()
    }
}

data class GptResponse(
    val id: String, // 응답 ID
    val objectType: String, // 응답 타입 (예: "chat.completion")
    val created: Long, // 응답 생성 시간 (Unix Time)
    val model: String, // 사용된 모델 이름
    val usage: Usage?, // 토큰 사용량 정보
    val choices: List<Choice> // 응답 메시지 선택
)

data class Choice(
    val message: Message, // 생성된 메시지
    val finishReason: String, // 응답 종료 이유 (예: "stop", "length")
    val index: Int // 선택지 인덱스
)

data class Usage(
    val promptTokens: Int, // 요청에 사용된 토큰 수
    val completionTokens: Int, // 생성된 응답 토큰 수
    val totalTokens: Int // 전체 사용된 토큰 수
)
