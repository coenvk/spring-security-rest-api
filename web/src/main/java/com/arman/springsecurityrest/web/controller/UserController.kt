package com.arman.springsecurityrest.web.controller

import com.arman.springsecurityrest.common.model.request.user.LoginRequest
import com.arman.springsecurityrest.common.model.request.user.RegisterRequest
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.servlet.ModelAndView

@Controller
class UserController {

    @RequestMapping("/user/register", method = [RequestMethod.GET])
    fun register(): ModelAndView {
        return ModelAndView("register", "request", RegisterRequest())
    }

    @RequestMapping("/user/register", method = [RequestMethod.POST])
    fun redirectRegister(@ModelAttribute request: RegisterRequest): ModelAndView {
        return ModelAndView("login", "request", LoginRequest())
    }

}