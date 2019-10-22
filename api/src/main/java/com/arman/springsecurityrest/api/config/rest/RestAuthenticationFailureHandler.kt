package com.arman.springsecurityrest.api.config.rest

import com.arman.springsecurityrest.api.service.LoginAttemptService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.security.core.AuthenticationException
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler
import org.springframework.stereotype.Component
import javax.security.auth.login.LoginException
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Component
class RestAuthenticationFailureHandler : SimpleUrlAuthenticationFailureHandler() {

    @Autowired
    private lateinit var loginAttemptService: LoginAttemptService

    @Throws(LoginException::class)
    override fun onAuthenticationFailure(request: HttpServletRequest?, response: HttpServletResponse?, exception: AuthenticationException?) {
        request?.let { loginAttemptService.loginFailed(it) }
        response?.let {
            it.status = HttpStatus.UNAUTHORIZED.value()
        }
    }

}