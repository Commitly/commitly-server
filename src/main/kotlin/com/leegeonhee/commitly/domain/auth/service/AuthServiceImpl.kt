package com.leegeonhee.commitly.domain.auth.service

import com.leegeonhee.commitly.domain.auth.domain.entity.User
import com.leegeonhee.commitly.domain.auth.domain.internal.github.GithubOAuth2Client
import com.leegeonhee.commitly.domain.auth.domain.model.GithubProperties
import com.leegeonhee.commitly.domain.auth.domain.model.OAuthAccessTokenRequest
import com.leegeonhee.commitly.domain.auth.domain.model.OAuthTokensResponse
import com.leegeonhee.commitly.domain.auth.domain.model.user.GithubUserInfo
import com.leegeonhee.commitly.domain.auth.domain.model.user.git.GithubCommitResponse
import com.leegeonhee.commitly.domain.auth.domain.repository.UserRepository
import com.leegeonhee.commitly.gloabl.exception.CustomException
import org.springframework.http.*
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate
import org.springframework.web.client.exchange
import org.springframework.web.client.getForObject

@Service
class AuthServiceImpl(
    private val userRepository: UserRepository,
    private val githubOAuth2Client: GithubOAuth2Client
) : AuthService {
    override fun githubOAuth2SignIn(code: String): GithubCommitResponse? {
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
        val user = users.firstOrNull() ?: userRepository.save(
           User(
               userId = githubUserInfo.id,
               login = githubUserInfo.login?: throw CustomException(HttpStatus.BAD_REQUEST, "아마 오류 뜰일 없을듯"),
               name = githubUserInfo.name?: throw CustomException(HttpStatus.BAD_REQUEST, "아마 오류 뜰일 없을듯")
           )
        )
        println(user.login)
        val userCommits = githubOAuth2Client.myGitLog(
            token = githubAccessToken.accessToken,
            user = user.login
        )
        return userCommits
    }
}