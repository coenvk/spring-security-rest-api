package com.arman.springsecurityrest.api.service.authentication

import com.arman.springsecurityrest.api.service.UserService
import org.springframework.security.core.Authentication
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

class TokenAuthenticationService(secret: String, userService: UserService) {

    companion object {

        private const val HEADER = "X-Authentication-Token"

    }

    private val tokenHandler = TokenEncoder(secret, userService)

    fun setToken(response: HttpServletResponse, authentication: UserAuthentication) {
        val user = authentication.details
        response.addHeader(HEADER, tokenHandler.encode(user))
    }

    fun getToken(request: HttpServletRequest): Authentication? {
        val token = request.getHeader(HEADER)
        if (token != null) {
            val user = tokenHandler.decode(token)
            return UserAuthentication(user, true)
        }
        return null
    }

}