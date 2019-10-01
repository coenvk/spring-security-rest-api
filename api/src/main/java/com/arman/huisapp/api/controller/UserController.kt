package com.arman.huisapp.api.controller

import com.arman.huisapp.common.model.User
import com.arman.huisapp.common.model.request.user.LoginRequest
import com.arman.huisapp.common.model.request.user.RegisterRequest
import com.arman.huisapp.api.service.UserService
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import org.springframework.validation.Errors
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RestController
import retrofit2.http.FormUrlEncoded
import javax.annotation.PostConstruct
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
    fun login(@Valid request: LoginRequest, errors: Errors) = service.login(request)

    @FormUrlEncoded
    @PostMapping(
        value = ["/api/users"],
        consumes = [MediaType.APPLICATION_FORM_URLENCODED_VALUE],
        produces = [MediaType.APPLICATION_JSON_VALUE]
    )
    fun register(@Valid request: RegisterRequest, errors: Errors) = service.register(request)

    @GetMapping(
        value = ["/api/users"],
        produces = [MediaType.APPLICATION_JSON_VALUE]
    )
    fun findAll() = service.userRepository.findAll()

    @GetMapping(
        value = ["/api/users/{userId}"],
        produces = [MediaType.APPLICATION_JSON_VALUE]
    )
    fun findById(@Valid @PathVariable userId: Long): User {
        return service.userRepository.findById(userId).map {
            it
        }.orElseThrow { Exception() }
    }

}