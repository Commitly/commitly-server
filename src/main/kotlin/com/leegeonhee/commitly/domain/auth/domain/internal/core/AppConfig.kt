package com.leegeonhee.commitly.domain.auth.domain.internal.core


import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.client.RestTemplate

@Configuration
class AppConfig {
    @Bean
    fun restTemplate() = RestTemplate()
}
