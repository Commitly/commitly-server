//package com.leegeonhee.commitly.gloabl.util
//
//import org.springframework.http.HttpStatus
//import org.springframework.http.ResponseEntity
//import org.springframework.web.bind.annotation.ExceptionHandler
//import org.springframework.web.bind.annotation.RestControllerAdvice
//
//@RestControllerAdvice
//class RateLimitExceptionHandler {
//    @ExceptionHandler(RateLimitExceededException::class)
//    fun handleRateLimitException(ex: RateLimitExceededException): ResponseEntity<Map<String, String>> {
//        return ResponseEntity
//            .status(HttpStatus.TOO_MANY_REQUESTS)
//            .body(mapOf("error" to ex.message!!))
//    }
//}