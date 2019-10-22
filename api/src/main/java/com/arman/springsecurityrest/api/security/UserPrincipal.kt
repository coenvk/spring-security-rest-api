package com.arman.springsecurityrest.api.security

import com.arman.springsecurityrest.common.model.User
import org.slf4j.LoggerFactory
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails

class UserPrincipal(
        val user: User
) : UserDetails {

    companion object {

        private val Logger = LoggerFactory.getLogger(UserPrincipal::class.java)

    }

    override fun getUsername(): String {
        return user.email
    }

    override fun getPassword(): String {
        return user.password
    }

    override fun getAuthorities(): MutableCollection<out GrantedAuthority> {
        val authorities = mutableSetOf<GrantedAuthority>()
        user.roles.forEach {
            authorities.add(SimpleGrantedAuthority(it.name))
        }
        val privileges = user.roles.map {
            it.privileges
        }.flatten().distinctBy { it.id }
        privileges.forEach {
            authorities.add(SimpleGrantedAuthority(it.name))
        }
        return authorities
    }

    override fun isEnabled(): Boolean {
        return user.enabled
    }

    override fun isCredentialsNonExpired(): Boolean {
        return true
    }

    override fun isAccountNonExpired(): Boolean {
        return true
    }

    override fun isAccountNonLocked(): Boolean {
        return true
    }

}