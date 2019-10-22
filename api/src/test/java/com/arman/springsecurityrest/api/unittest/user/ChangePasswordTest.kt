package com.arman.springsecurityrest.api.unittest.user

import org.junit.FixMethodOrder
import org.junit.Test
import org.junit.runners.MethodSorters
import org.springframework.http.HttpStatus

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
class ChangePasswordTest : UserTest() {

    @Test
    fun test1_LoggedIn_ChangePassword_Correct() {
        val user = registerTestUser()

        user.let {
            it.password = passwordEncoder.encode("newPassword")
            userRepository.save(it)
        }

        val loginResponse = loginUser(user.email, password = "newPassword")
        loginResponse.then().assertThat().statusCode(HttpStatus.OK.value())
        Logger.info(loginResponse.statusLine)
    }

}