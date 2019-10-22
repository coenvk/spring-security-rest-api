package com.arman.springsecurityrest.api.unittest.token

import com.arman.springsecurityrest.api.service.TokenService
import com.arman.springsecurityrest.api.unittest.ApiTest
import com.arman.springsecurityrest.common.model.Token
import org.junit.Assert
import org.junit.FixMethodOrder
import org.junit.Test
import org.junit.runners.MethodSorters
import org.springframework.beans.factory.annotation.Autowired
import java.time.LocalDate
import java.util.*
import kotlin.test.assertFalse

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
class TokenTest : ApiTest() {

    @Autowired
    lateinit var tokenService: TokenService

    private var tokenId: Long = 0

    override fun beforeEach() {
        super.beforeEach()

        val user = registerTestUser()

        val token = UUID.randomUUID().toString()
        val authToken = Token(token = token, user = user, tokenType = "TOKEN_EMAIL_VERIFICATION")
        authToken.expiryDate = LocalDate.now().minusDays(2)

        tokenRepository.save(authToken)

        tokenId = authToken.id
    }

    @Test
    fun test1_VerifyTokenExpired() {
        val token = tokenRepository.findById(tokenId)
        Assert.assertTrue(token.isPresent)
        val expiredTokens = tokenRepository.findAllByExpiryDateLessThanEqual(LocalDate.now())
        Assert.assertTrue(expiredTokens.any { t -> t == token.get() })
    }

    @Test
    fun test2_PurgeToken() {
        tokenService.purgeExpired()
        assertFalse(tokenRepository.findById(tokenId).isPresent)
    }

}