package com.leegeonhee.commitly.gloabl.common.annotation


import org.springframework.security.core.annotation.AuthenticationPrincipal

@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.VALUE_PARAMETER)
@AuthenticationPrincipal(expression = "#this == null ? -1L : #this.id")
// if this == null (i.e., anonymous user) return -1 else return userId
annotation class GetAuthenticatedId
