package com.arman.springsecurityrest.api.service

import com.arman.springsecurityrest.api.error.exception.InvalidPasswordException
import com.arman.springsecurityrest.api.error.exception.InvalidRequestParameterException
import com.arman.springsecurityrest.api.listener.event.OnRegistrationCompleteEvent
import com.arman.springsecurityrest.api.repository.UserRepository
import com.arman.springsecurityrest.api.security.UserPrincipal
import com.arman.springsecurityrest.common.model.User
import com.arman.springsecurityrest.common.model.request.user.ChangePasswordRequest
import com.arman.springsecurityrest.common.model.request.user.RegisterRequest
import com.arman.springsecurityrest.common.model.user
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.ApplicationEventPublisher
import org.springframework.security.authentication.DisabledException
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import javax.annotation.PostConstruct
import javax.persistence.EntityExistsException
import javax.persistence.EntityNotFoundException

@Service
class UserService : EntityService<User>, UserDetailsService {

    companion object {

        private val Logger = LoggerFactory.getLogger(UserService::class.java)

    }

    @Autowired
    lateinit var userRepository: UserRepository

    @Autowired
    private lateinit var loginAttemptService: LoginAttemptService

    @Autowired
    private lateinit var eventPublisher: ApplicationEventPublisher

    @Autowired
    private lateinit var passwordEncoder: PasswordEncoder

    @PostConstruct
    fun init() {
        Logger.info("Constructed ${javaClass.name}")
        Logger.info(userRepository.toString())
    }

    @Throws(EntityExistsException::class, InvalidRequestParameterException::class)
    fun register(request: RegisterRequest): User {
        if (request.email != null && request.name != null && request.password != null) {
            val user = user {
                name(request.name!!)
                email(request.email!!)
                password(passwordEncoder.encode(request.password))
            }
            val savedUser = userRepository.save(user)
            savedUser.let { eventPublisher.publishEvent(OnRegistrationCompleteEvent(it)) }
            return savedUser
        }
        throw InvalidRequestParameterException("One of the request parameters is invalid.")
    }

    @Throws(EntityNotFoundException::class, InvalidPasswordException::class)
    fun changePassword(request: ChangePasswordRequest): User {
        return userRepository.findByEmailAndEnabled(SecurityContextHolder.getContext().authentication.name).map {
            if (!passwordEncoder.matches(request.oldPassword, it.password)) {
                throw InvalidPasswordException("Invalid old password.")
            }
            it.password = passwordEncoder.encode(request.password)
            userRepository.save(it)
        }.orElseThrow { EntityNotFoundException("User not found.") }
    }

    @Throws(UsernameNotFoundException::class, DisabledException::class)
    override fun loadUserByUsername(email: String): UserDetails {
        if (loginAttemptService.isBlocked()) {
            throw DisabledException("User is blocked, because of too many failed attempts.")
        }

        return userRepository.findByEmail(email).map {
            if (!it.enabled) {
                throw DisabledException("User is disabled.")
            }
            UserPrincipal(it)
        }.orElseThrow { UsernameNotFoundException("The user with email $email was not found.") }
    }

}