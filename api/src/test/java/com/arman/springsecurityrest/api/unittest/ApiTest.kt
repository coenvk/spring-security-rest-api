package com.arman.springsecurityrest.api.unittest

import com.arman.springsecurityrest.api.Api
import com.arman.springsecurityrest.api.config.MethodSecurityConfig
import com.arman.springsecurityrest.api.config.WebSecurityConfig
import com.arman.springsecurityrest.api.repository.DeviceRepository
import com.arman.springsecurityrest.api.repository.TokenRepository
import com.arman.springsecurityrest.api.repository.UserRepository
import com.arman.springsecurityrest.api.service.LoginAttemptService
import com.arman.springsecurityrest.common.model.User
import com.arman.springsecurityrest.common.model.user
import io.restassured.RestAssured
import io.restassured.response.Response
import org.junit.After
import org.junit.Before
import org.junit.runner.RunWith
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner
import java.util.*

@RunWith(SpringJUnit4ClassRunner::class)
@SpringBootTest(classes = [Api::class, WebSecurityConfig::class, MethodSecurityConfig::class], webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
abstract class ApiTest {

    protected companion object {

        val Logger: Logger = LoggerFactory.getLogger(ApiTest::class.java)

        const val ApiUrl = "/api"
        const val LoginUrl = "/login"
        const val LogoutUrl = "/logout"

    }

    @Autowired
    protected lateinit var userRepository: UserRepository

    @Autowired
    protected lateinit var tokenRepository: TokenRepository

    @Autowired
    protected lateinit var deviceRepository: DeviceRepository

    @Autowired
    protected lateinit var passwordEncoder: PasswordEncoder

    @Autowired
    protected lateinit var loginAttemptService: LoginAttemptService

    @Value("\${local.server.port}")
    var port: Int = 0

    @Before
    fun beforeEach() {
        Logger.info("Server listening on http://localhost:$port")

        RestAssured.port = port
        RestAssured.baseURI = "http://localhost"

        loginAttemptService.init()
    }

    @After
    fun afterEach() {
        loginAttemptService.reset()

        Logger.info("Truncating all tables and resetting id sequences")

        userRepository.deleteAll()
        userRepository.resetIdSequence()
        tokenRepository.deleteAll()
        tokenRepository.resetIdSequence()
        deviceRepository.deleteAll()
        deviceRepository.resetIdSequence()
    }

    protected fun randomShortUUID(): String {
        return UUID.randomUUID().toString().substringBefore('-')
    }

    protected fun registerTestUser(name: String = randomShortUUID(), password: String = "test", email: String = "${randomShortUUID()}@test.com"): User {
        Logger.info("Setting up test user account...")

        var user = userRepository.findByEmail(email).map { it }.orElse(null)
        if (user == null) {
            user = user {
                name(name)
                password(passwordEncoder.encode(password))
                email(email)
                enabled(true)
            }
            userRepository.save(user)
        } else {
            user.password = passwordEncoder.encode(password)
            userRepository.save(user)
        }

        return user
    }

    protected fun loginUser(email: String, password: String = "test"): Response {
        return RestAssured.given()
                .header("Content-Type", "application/x-www-form-urlencoded")
                .formParam("email", email)
                .formParam("password", password)
                .request().post(LoginUrl)
    }

    protected fun logoutUser(): Response {
        return RestAssured.given()
                .request().post(LogoutUrl)
    }

}