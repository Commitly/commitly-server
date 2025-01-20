package com.leegeonhee.commitly.domain.auth.service

import com.leegeonhee.commitly.domain.auth.domain.model.RefreshToken
import com.leegeonhee.commitly.gloabl.common.BaseResponse
import com.leegeonhee.commitly.gloabl.jwt.JwtInfo


interface AuthService {
    fun githubOAuth2SignIn(code: String): BaseResponse<JwtInfo>
    fun refreshToken(refreshToken: RefreshToken): BaseResponse<String>
}