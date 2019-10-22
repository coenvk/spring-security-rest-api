package com.arman.springsecurityrest.api.listener

import com.arman.springsecurityrest.api.listener.event.OnRegistrationCompleteEvent
import com.arman.springsecurityrest.api.service.TokenService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.ApplicationListener
import org.springframework.stereotype.Component

@Component
class RegistrationListener : ApplicationListener<OnRegistrationCompleteEvent> {

    @Autowired
    private lateinit var tokenService: TokenService

    override fun onApplicationEvent(event: OnRegistrationCompleteEvent) {
        tokenService.createEmailVerificationToken(event.user)
        // TODO: send email with token like: http://{localhost}:3000/confirmRegistration?token={token}
    }

}