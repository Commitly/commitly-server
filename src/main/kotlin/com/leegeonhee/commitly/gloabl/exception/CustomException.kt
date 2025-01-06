package com.leegeonhee.commitly.gloabl.exception

import org.springframework.http.HttpStatus
import org.springframework.http.HttpStatusCode

class CustomException(
    val status: HttpStatus,
    override val message:String
) : RuntimeException()