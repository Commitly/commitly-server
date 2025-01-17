package com.leegeonhee.commitly.gloabl.exception

import com.leegeonhee.commitly.gloabl.jwt.exception.CustomErrorCode
import org.springframework.http.HttpStatus
import org.springframework.http.HttpStatusCode

class CustomException(
    val status: HttpStatus,
    override val message:String
) : RuntimeException()
class CustomExceptionTwo(
    val customErrorCode: CustomErrorCode
) : RuntimeException()