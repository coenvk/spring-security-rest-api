package com.arman.springsecurityrest.api.config.rest

import com.arman.springsecurityrest.api.service.LoginAttemptService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.Authentication
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler
import org.springframework.security.web.savedrequest.HttpSessionRequestCache
import org.springframework.stereotype.Component
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Component
class RestAuthenticationSuccessHandler : SimpleUrlAuthenticationSuccessHandler() {

    private val requestCache = HttpSessionRequestCache()

    @Autowired
    private lateinit var loginAttemptService: LoginAttemptService

    override fun onAuthenticationSuccess(request: HttpServletRequest?, response: HttpServletResponse?, auth: Authentication?) {
        request?.let { loginAttemptService.loginSucceeded(it) }

        val savedRequest = requestCache.getRequest(request, response)
        if (savedRequest == null) {
            clearAuthenticationAttributes(request)
        } else if (isAlwaysUseDefaultTargetUrl
                || (targetUrlParameter != null
                        && request != null
                        && request.getParameter(targetUrlParameter).isNotEmpty())) {
            requestCache.removeRequest(request, response)
            clearAuthenticationAttributes(request)
        } else {
            clearAuthenticationAttributes(request)
        }
    }

}