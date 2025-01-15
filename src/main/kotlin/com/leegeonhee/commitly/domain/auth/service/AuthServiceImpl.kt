package com.leegeonhee.commitly.domain.auth.service

import com.leegeonhee.commitly.domain.auth.domain.entity.UserEntity
import com.leegeonhee.commitly.domain.auth.domain.internal.github.GithubOAuth2Client
import com.leegeonhee.commitly.domain.auth.domain.repository.UserRepository
import com.leegeonhee.commitly.gloabl.common.BaseResponse
import com.leegeonhee.commitly.gloabl.exception.CustomException
import com.leegeonhee.commitly.gloabl.jwt.JwtInfo
import com.leegeonhee.commitly.gloabl.jwt.JwtUtils
import org.springframework.http.*
import org.springframework.stereotype.Service

@Service
class AuthServiceImpl(
    private val userRepository: UserRepository,
    private val githubOAuth2Client: GithubOAuth2Client,
    private val jwtUtils: JwtUtils
) : AuthService {
    override fun githubOAuth2SignIn(code: String): BaseResponse<JwtInfo> {
        val githubAccessToken = githubOAuth2Client.getAccessToken(code) ?: throw CustomException(
            status = HttpStatus.UNAUTHORIZED,
            message = "너가 잘못했음"
        )
        println(githubAccessToken)
        val githubUserInfo = githubOAuth2Client.getUserInfo(
            githubAccessToken.accessToken ?: throw CustomException(
                status = HttpStatus.UNAUTHORIZED,
                message = "이상한 코드를 왜 넣음??"
            )
        )
        val userId = githubUserInfo?.id ?: throw CustomException(
            status = HttpStatus.BAD_REQUEST,
            message = "GitHub 사용자 ID가 없습니다."
        )
        val users = userRepository.findByUserId(userId)
        users.firstOrNull() ?: userRepository.save(
           UserEntity(
               userId = githubUserInfo.id,
               login = githubUserInfo.login?: throw CustomException(HttpStatus.BAD_REQUEST, "아마 오류 뜰일 없을듯"),
               name = githubUserInfo.name?: throw CustomException(HttpStatus.BAD_REQUEST, "아마 오류 뜰일 없을듯"),
               responses = mutableListOf(),
           )
        )
        val nowUser =  userRepository.findByUserId(userId)
        val jwt = jwtUtils.generate(nowUser.first())
        println(jwt)
        return BaseResponse(
            status = 200,
            message = "로그인 되었음",
            data = jwt
        )

    }
}