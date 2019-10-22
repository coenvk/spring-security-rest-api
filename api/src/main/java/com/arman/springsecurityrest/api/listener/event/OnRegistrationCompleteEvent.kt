package com.arman.springsecurityrest.api.listener.event

import com.arman.springsecurityrest.common.model.User
import org.springframework.context.ApplicationEvent

class OnRegistrationCompleteEvent(val user: User) : ApplicationEvent(user)