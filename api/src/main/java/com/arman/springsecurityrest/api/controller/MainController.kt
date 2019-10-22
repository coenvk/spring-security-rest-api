package com.arman.springsecurityrest.api.controller

import com.arman.springsecurityrest.api.service.TokenService
import com.arman.springsecurityrest.api.service.UserService
import com.arman.springsecurityrest.common.model.User
import com.arman.springsecurityrest.common.model.request.user.ChangePasswordRequest
import com.arman.springsecurityrest.common.model.request.user.ResetPasswordRequest
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.stereotype.Controller
import org.springframework.validation.Errors
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.ResponseBody
import retrofit2.http.FormUrlEncoded
import javax.servlet.http.HttpServletRequest
import javax.validation.Valid

@Controller
class MainController {

    @Autowired
    private lateinit var tokenService: TokenService

    @Autowired
    private lateinit var userService: UserService

    @GetMapping(
            value = ["/confirmRegistration"],
            produces = [MediaType.APPLICATION_JSON_VALUE]
    )
    @ResponseBody
    fun confirmRegistration(request: HttpServletRequest, @RequestParam("token") token: String): ResponseEntity<User> {
        return ResponseEntity.ok(tokenService.validateEmailVerificationToken(token))
    }

    @GetMapping(
            value = ["/resetPassword"],
            produces = [MediaType.APPLICATION_JSON_VALUE]
    )
    @ResponseBody
    fun loginForPasswordReset(request: HttpServletRequest, @RequestParam("id") userId: Long, @RequestParam("token") token: String): ResponseEntity<User> {
        return ResponseEntity.ok(tokenService.validatePasswordResetToken(userId, token))
    }

    @FormUrlEncoded
    @PostMapping(
            value = ["/resetPassword"],
            consumes = [MediaType.APPLICATION_FORM_URLENCODED_VALUE],
            produces = [MediaType.APPLICATION_JSON_VALUE]
    )
    @ResponseBody
    fun resetPassword(@Valid request: ResetPasswordRequest, errors: Errors) {
        tokenService.createPasswordResetToken(request)
    }

    @FormUrlEncoded
    @PostMapping(
            value = ["/changePassword"],
            consumes = [MediaType.APPLICATION_FORM_URLENCODED_VALUE],
            produces = [MediaType.APPLICATION_JSON_VALUE]
    )
    @PreAuthorize("isAuthenticated()")
    @ResponseBody
    fun changePassword(@Valid request: ChangePasswordRequest, errors: Errors) {
        userService.changePassword(request)
    }

}