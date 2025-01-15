package com.leegeonhee.commitly.gloabl.jwt

import com.leegeonhee.commitly.domain.auth.domain.entity.UserEntity
import com.leegeonhee.commitly.gloabl.jwt.exception.type.JwtErrorType
import io.jsonwebtoken.ExpiredJwtException
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.MalformedJwtException
import io.jsonwebtoken.UnsupportedJwtException
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.stereotype.Component
import java.nio.charset.StandardCharsets
import java.security.SignatureException
import java.util.*
import javax.crypto.SecretKey
import javax.crypto.spec.SecretKeySpec

@Component
class JwtUtils(
    private val jwtProperties: JwtProperties,
    private val userDetailsService: UserDetailsService,
) {

    private val secretKey: SecretKey = SecretKeySpec(
        jwtProperties.secretKey.toByteArray(StandardCharsets.UTF_8),
        "HmacSHA256"
    )
    fun getUsername(token: String): String {
        return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).payload.get(
            "login",
            String::class.java
        )
    }

    fun checkTokenInfo(token: String): JwtErrorType {
        try {
            Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token)
            return JwtErrorType.OK
        } catch (e: ExpiredJwtException) {
            return JwtErrorType.ExpiredJwtException
        } catch (e: SignatureException) {
            return JwtErrorType.SignatureException
        } catch (e: MalformedJwtException) {
            return JwtErrorType.MalformedJwtException
        } catch (e: UnsupportedJwtException) {
            return JwtErrorType.UnsupportedJwtException
        } catch (e: IllegalArgumentException) {
            return JwtErrorType.IllegalArgumentException
        } catch (e: Exception) {
            return JwtErrorType.UNKNOWN_EXCEPTION
        }
    }

    fun getToken(token: String): String {
        return token.removePrefix("Bearer ")
    }

    fun generate(user: UserEntity): JwtInfo {
        val accessToken = createToken(
            user = user,
            tokenExpired = jwtProperties.accessExpired
        )
        val refreshToken = createToken(
            user = user,
            tokenExpired = jwtProperties.refreshExpired
        )


        return JwtInfo("Bearer $accessToken", "Bearer $refreshToken")
    }

    fun getAuthentication(token: String): Authentication {
        val tokenWithoutBearer = getToken(token)
        println("Getting authentication for token: $tokenWithoutBearer")
        val username = getUsername(tokenWithoutBearer)
        println("Username from token: $username")
        val userDetails = userDetailsService.loadUserByUsername(username)
        println("UserDetails loaded: ${userDetails.username}")
        return UsernamePasswordAuthenticationToken(userDetails, null, userDetails.authorities)
    }
    fun refreshToken(user: UserEntity): String {
        return "Bearer " + createToken(
            user = user,
            tokenExpired = jwtProperties.accessExpired
        )
    }

    private fun createToken(user: UserEntity, tokenExpired: Long): String {
        val now: Long = Date().time
        return Jwts.builder()
            .subject(user.login)  // subject 추가
            .claim("id", user.id)
            .claim("role", user.role)
                .claim("login", user.login)
            .issuedAt(Date(now))
            .expiration(Date(now + tokenExpired))
            .signWith(secretKey)
            .compact()
    }

}