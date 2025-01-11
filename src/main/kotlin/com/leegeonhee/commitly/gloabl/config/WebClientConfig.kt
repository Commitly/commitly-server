package com.leegeonhee.commitly.gloabl.config

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.web.reactive.function.client.WebClient

@Configuration
class WebClientConfig {
    @Value("\${github.token}")
    private lateinit var token: String


    @Value("\${gpt.key}")
    private lateinit var key: String
    @Bean
    fun webClient(): WebClient {
        return WebClient.builder()
            .baseUrl("https://api.github.com/graphql")
            .defaultHeader(HttpHeaders.AUTHORIZATION, "Bearer $token")
            .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
            .build()
    }

}