package com.leegeonhee.commitly.domain.user.domain.model

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.stereotype.Component

@Component
@ConfigurationProperties(prefix = "spring.security.oauth2.client.registration.github")
data class GithubProperties(
    var clientId: String? = null,
    var clientSecret: String? = null
)
