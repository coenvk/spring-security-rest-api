package com.arman.springsecurityrest.api.service.authentication

import org.springframework.security.core.Authentication
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.UserDetails

class UserAuthentication(private val user: UserDetails, private var authenticated: Boolean = false) : Authentication {

    override fun setAuthenticated(authenticated: Boolean) {
        this.authenticated = authenticated
    }

    override fun getName(): String = user.username

    override fun getCredentials(): String = user.password

    override fun getPrincipal(): String = user.username

    override fun isAuthenticated() = authenticated

    override fun getDetails() = user

    override fun getAuthorities(): MutableCollection<out GrantedAuthority> = user.authorities

}