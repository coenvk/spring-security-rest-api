package com.arman.springsecurityrest.api.service

import com.arman.springsecurityrest.api.error.exception.InvalidTokenException
import com.arman.springsecurityrest.api.repository.TokenRepository
import com.arman.springsecurityrest.api.repository.UserRepository
import com.arman.springsecurityrest.api.security.UserPrincipal
import com.arman.springsecurityrest.api.service.authentication.UserAuthentication
import com.arman.springsecurityrest.common.model.Token
import com.arman.springsecurityrest.common.model.User
import com.arman.springsecurityrest.common.model.request.user.ResetPasswordRequest
import com.arman.springsecurityrest.common.model.token
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Service
import java.security.MessageDigest
import java.time.LocalDate
import java.util.*
import javax.persistence.EntityNotFoundException

@Service
class TokenService : EntityService<Token> {

    companion object {

        const val TokenEmailVerification = "TOKEN_EMAIL_VERIFICATION"
        const val TokenPasswordReset = "TOKEN_PASSWORD_RESET"
    }

    @Autowired
    lateinit var tokenRepository: TokenRepository

    @Autowired
    private lateinit var userRepository: UserRepository

    @Scheduled(cron = "\${purge.cron.expression:-}")
    fun purgeExpired() {
        val now = LocalDate.now()
        tokenRepository.deleteAllByExpiryDateLessThanEqual(now)
    }

    fun validateEmailVerificationToken(token: String): User {
        val user = validateToken(token, TokenEmailVerification)
        user.let {
            it.enabled = true
            return userRepository.save(it)
        }
    }

    @Throws(InvalidTokenException::class)
    fun validatePasswordResetToken(userId: Long, token: String): User {
        val user = validateToken(token, TokenPasswordReset)
        if (user.id != userId) {
            throw InvalidTokenException("Invalid token.")
        }
        user.let {
            val auth = UserAuthentication(UserPrincipal(it), true)
            SecurityContextHolder.getContext().authentication = auth
            return it
        }
    }

    fun createEmailVerificationToken(user: User): User {
        createToken(user, TokenEmailVerification)
        return user
    }

    @Throws(EntityNotFoundException::class, IllegalArgumentException::class)
    fun createPasswordResetToken(request: ResetPasswordRequest): User {
        if (request.email != null) {
            return userRepository.findByEmailAndEnabled(request.email!!).map {
                createToken(it, TokenPasswordReset)
                it
            }.orElseThrow { EntityNotFoundException("User not found.") }
        }
        throw IllegalArgumentException("The email parameter of the request is invalid.")
    }

    @Throws(EntityNotFoundException::class, InvalidTokenException::class)
    private fun validateToken(tokenText: String, tokenType: String): User {
        val token = tokenRepository.findByTokenAndTokenTypeAndExpiryDateGreaterThan(tokenText, tokenType, LocalDate.now())
        return token.map {
            if (it.user != null) {
                it.user!!
            }
            throw EntityNotFoundException("There exists no user that is linked to this token")
        }.orElseThrow { InvalidTokenException("Invalid token. Token might be expired.") }
    }

    private fun createToken(user: User, tokenType: String) {
        val salt = MessageDigest.getInstance("SHA-512")
        val uuid = UUID.randomUUID().toString()
        salt.update(uuid.toByteArray(Charsets.UTF_8))
        val tokenText = Base64.getUrlEncoder().encodeToString(salt.digest())
        val token = token {
            token(tokenText)
            user(user)
            tokenType(tokenType)
            expiryDate(LocalDate.now().plusDays(1))
        }
        tokenRepository.save(token)
    }

}