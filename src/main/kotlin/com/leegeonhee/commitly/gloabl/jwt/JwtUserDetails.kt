package com.leegeonhee.commitly.gloabl.jwt

import com.leegeonhee.commitly.domain.auth.domain.model.user.user.User
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails


class JwtUserDetails(
    val user: User
) : UserDetails {

    val id: Long? = user.id

    override fun getAuthorities(): MutableCollection<out GrantedAuthority> {
        val authorities: MutableCollection<GrantedAuthority> = ArrayList()

        authorities.add(SimpleGrantedAuthority(user.role.name))

        return authorities
    }

    override fun getPassword(): String {
        return "없음"
    }

    override fun getUsername(): String {
        return user.name
    }

    override fun isAccountNonExpired(): Boolean {
        return true
    }

    override fun isAccountNonLocked(): Boolean {
        return true
    }

    override fun isCredentialsNonExpired(): Boolean {
        return true
    }

    override fun isEnabled(): Boolean {
        return true
    }
}