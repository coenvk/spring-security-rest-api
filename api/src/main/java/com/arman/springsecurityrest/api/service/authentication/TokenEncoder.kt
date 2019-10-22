package com.arman.springsecurityrest.api.service.authentication

import com.arman.springsecurityrest.api.service.UserService
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import org.springframework.security.core.userdetails.UserDetails

class TokenEncoder(private val secret: String, private val userService: UserService) {

    fun decode(token: String): UserDetails {
        val username = Jwts.parser()
                .setSigningKey(secret)
                .parseClaimsJws(token)
                .body
                .subject
        return userService.loadUserByUsername(username)
    }

    fun encode(user: UserDetails): String {
        return Jwts.builder()
                .setSubject(user.username)
                .signWith(SignatureAlgorithm.HS512, secret)
                .compact()
    }

}