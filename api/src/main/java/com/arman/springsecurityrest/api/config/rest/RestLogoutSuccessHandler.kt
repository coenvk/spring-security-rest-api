package com.arman.springsecurityrest.api.config.rest

import org.springframework.security.core.Authentication
import org.springframework.security.web.authentication.logout.SimpleUrlLogoutSuccessHandler
import org.springframework.stereotype.Component
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Component
class RestLogoutSuccessHandler : SimpleUrlLogoutSuccessHandler() {

    override fun onLogoutSuccess(request: HttpServletRequest?, response: HttpServletResponse?, auth: Authentication?) {
        auth?.isAuthenticated = false
    }

}