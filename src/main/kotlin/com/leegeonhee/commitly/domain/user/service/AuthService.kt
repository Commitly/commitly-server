package com.leegeonhee.commitly.domain.user.service

import com.leegeonhee.commitly.gloabl.common.BaseResponse
import com.leegeonhee.commitly.gloabl.jwt.JwtInfo


interface AuthService {
    fun githubOAuth2SignIn(code: String): BaseResponse<JwtInfo>
}