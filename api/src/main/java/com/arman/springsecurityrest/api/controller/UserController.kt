package com.arman.springsecurityrest.api.controller

import com.arman.springsecurityrest.api.security.UserPrincipal
import com.arman.springsecurityrest.api.service.UserService
import com.arman.springsecurityrest.common.model.User
import com.arman.springsecurityrest.common.model.request.user.RegisterRequest
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.validation.Errors
import org.springframework.web.bind.annotation.*
import retrofit2.http.FormUrlEncoded
import javax.annotation.PostConstruct
import javax.persistence.EntityNotFoundException
import javax.validation.Valid

@RestController
@RequestMapping("/api/users")
class UserController {

    companion object {

        private val Logger = LoggerFactory.getLogger(UserController::class.java)

    }

    @PostConstruct
    fun init() {
        Logger.info("Constructed ${javaClass.name}")
        Logger.info(userService.toString())
    }

    @Autowired
    private lateinit var userService: UserService

    @FormUrlEncoded
    @PostMapping(
            value = [""],
            consumes = [MediaType.APPLICATION_FORM_URLENCODED_VALUE],
            produces = [MediaType.APPLICATION_JSON_VALUE]
    )
    @ResponseBody
    fun register(@Valid request: RegisterRequest, errors: Errors): ResponseEntity<User> {
        return ResponseEntity.ok(userService.register(request))
    }

    @GetMapping(
            value = [""],
            produces = [MediaType.APPLICATION_JSON_VALUE]
    )
    @PreAuthorize("hasAuthority('READ_PRIVILEGE')")
    @ResponseBody
    fun findAll(): ResponseEntity<MutableIterable<User>> {
        return ResponseEntity.ok(userService.userRepository.findAll())
    }

    @GetMapping(
            value = ["/{userId}"],
            produces = [MediaType.APPLICATION_JSON_VALUE]
    )
    @PreAuthorize("hasAuthority('READ_PRIVILEGE')")
    @ResponseBody
    fun findById(@Valid @PathVariable userId: Long): ResponseEntity<User> {
        return userService.userRepository.findById(userId).map {
            ResponseEntity.ok(it)
        }.orElseThrow { EntityNotFoundException("The user with id $userId could not be found.") }
    }

    @GetMapping(
            value = [""],
            produces = [MediaType.APPLICATION_JSON_VALUE],
            params = ["email"]
    )
    @PreAuthorize("hasAuthority('READ_PRIVILEGE')")
    @ResponseBody
    fun findByEmail(@Valid @RequestParam email: String): ResponseEntity<User> {
        return userService.userRepository.findByEmail(email).map {
            ResponseEntity.ok(it)
        }.orElseThrow { EntityNotFoundException("The user with email $email could not be found.") }
    }

    @GetMapping(
            value = ["/current"],
            produces = [MediaType.APPLICATION_JSON_VALUE]
    )
    @PreAuthorize("isAuthenticated()")
    @ResponseBody
    fun findLoggedInUser(): ResponseEntity<User> {
        val principal = SecurityContextHolder.getContext().authentication.principal as UserPrincipal
        return ResponseEntity.ok(principal.user)
    }

}