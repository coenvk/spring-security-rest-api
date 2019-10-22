package com.arman.springsecurityrest.api.unittest.user

import com.arman.springsecurityrest.api.service.LoginAttemptService
import org.junit.FixMethodOrder
import org.junit.Test
import org.junit.runners.MethodSorters
import org.springframework.http.HttpStatus

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
class BruteForceTest : UserTest() {

    @Test
    fun test1_Login_CorrectPassword() {
        val user = registerTestUser()

        val response = loginUser(user.email)
        response.then().assertThat().statusCode(HttpStatus.OK.value())
        Logger.info(response.statusLine)
    }

    @Test
    fun test2_Login_WrongPassword() {
        val user = registerTestUser()

        val response = loginUser(user.email, password = "XXXX")
        response.then().assertThat().statusCode(HttpStatus.UNAUTHORIZED.value())
        Logger.info(response.statusLine)
    }

    @Test
    fun test3_Login_WrongPassword_TooMuchAttempts() {
        val user = registerTestUser()

        loginUser(user.email) // reset attempt counter.

        for (i in 0 until LoginAttemptService.MAX_ATTEMPTS) {
            loginUser(user.email, password = "XXXX")
        }

        val response = loginUser(user.email)
        response.then().assertThat().statusCode(HttpStatus.UNAUTHORIZED.value())
        Logger.info(response.statusLine)
    }

}