package com.leegeonhee.commitly.gloabl.jwt

import com.leegeonhee.commitly.domain.user.domain.mapper.UserMapper
import com.leegeonhee.commitly.domain.user.domain.repository.UserRepository
import com.leegeonhee.commitly.gloabl.exception.CustomException
import com.leegeonhee.commitly.gloabl.jwt.exception.UserErrorCode
import org.springframework.http.HttpStatus
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class JwtUserDetailsService(
    private val userRepository: UserRepository,
    private val userMapper: UserMapper
) : UserDetailsService {

    @Transactional(readOnly = true)
    override fun loadUserByUsername(login: String): UserDetails {
        val user = JwtUserDetails(
            user = userMapper.toDomain(
                entity = userRepository.findByLogin(login) ?: throw CustomException(
                    status = HttpStatus.NOT_FOUND,
                    message = "ㅇㄴ",
                )
            )
        )
        println("아이디가 없나${user.username}")
        return user
    }

}