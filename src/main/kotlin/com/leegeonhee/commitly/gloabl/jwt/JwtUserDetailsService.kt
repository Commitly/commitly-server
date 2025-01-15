package com.leegeonhee.commitly.gloabl.jwt

import com.leegeonhee.commitly.domain.auth.domain.repository.UserRepository
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class JwtUserDetailsService (
    private val userRepository: UserRepository,
) : UserDetailsService {

    @Transactional(readOnly = true)
    override fun loadUserByUsername(login: String): UserDetails {
        return JwtUserDetails (
            user = userRepository.findByLogin(login)
        )
    }

}