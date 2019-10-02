package com.arman.huisapp.api.controller

import com.arman.huisapp.api.service.UserService
import com.arman.huisapp.common.model.User
import com.arman.huisapp.common.model.request.user.LoginRequest
import com.arman.huisapp.common.model.request.user.RegisterRequest
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.validation.Errors
import org.springframework.web.bind.annotation.*
import retrofit2.http.FormUrlEncoded
import javax.annotation.PostConstruct
import javax.persistence.EntityNotFoundException
import javax.validation.Valid

@RestController
class UserController {

    companion object {

        private val Logger = LoggerFactory.getLogger(UserController::class.java)

    }

    @PostConstruct
    fun init() {
        Logger.info("Constructed ${javaClass.name}")
        Logger.info(service.toString())
    }

    @Autowired
    private lateinit var service: UserService

    @FormUrlEncoded
    @PostMapping(
            value = ["/api/users/login"],
            consumes = [MediaType.APPLICATION_FORM_URLENCODED_VALUE],
            produces = [MediaType.APPLICATION_JSON_VALUE]
    )
    @ResponseBody
    fun login(@Valid request: LoginRequest, errors: Errors): ResponseEntity<User> {
        return ResponseEntity.ok(service.login(request))
    }

    @FormUrlEncoded
    @PostMapping(
            value = ["/api/users"],
            consumes = [MediaType.APPLICATION_FORM_URLENCODED_VALUE],
            produces = [MediaType.APPLICATION_JSON_VALUE]
    )
    @ResponseBody
    fun register(@Valid request: RegisterRequest, errors: Errors): ResponseEntity<User> {
        return ResponseEntity.ok(service.register(request))
    }

    @GetMapping(
            value = ["/api/users"],
            produces = [MediaType.APPLICATION_JSON_VALUE]
    )
    @ResponseBody
    fun findAll(): ResponseEntity<MutableIterable<User>> {
        return ResponseEntity.ok(service.userRepository.findAll())
    }

    @GetMapping(
            value = ["/api/users/{userId}"],
            produces = [MediaType.APPLICATION_JSON_VALUE]
    )
    @ResponseBody
    fun findById(@Valid @PathVariable userId: Long): ResponseEntity<User> {
        return service.userRepository.findById(userId).map {
            ResponseEntity.ok(it)
        }.orElseThrow { EntityNotFoundException("The user with id $userId could not be found.") }
    }

}