package com.arman.springsecurityrest.api.unittest.user

import io.restassured.RestAssured
import org.junit.FixMethodOrder
import org.junit.Test
import org.junit.runners.MethodSorters
import org.springframework.http.HttpStatus

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
class AuthenticationTest : UserTest() {

    @Test
    fun test1_Register_Correct() {
        val response = RestAssured.given().header("Content-Type", "application/x-www-form-urlencoded").formParam("name", randomShortUUID()).formParam("email", "${randomShortUUID()}@test.com").formParam("password", "test").request().post(RegisterUrl)
        response.then().assertThat().statusCode(HttpStatus.OK.value())
        Logger.info(response.statusLine)
    }

    @Test
    fun test2_Register_Existing() {
        val user = registerTestUser()

        val response = RestAssured.given().header("Content-Type", "application/x-www-form-urlencoded").formParam("name", user.name).formParam("email", user.email).formParam("password", "test").request().post(RegisterUrl)
        response.then().assertThat().statusCode(HttpStatus.INTERNAL_SERVER_ERROR.value())
        Logger.info(response.statusLine)
    }

    @Test
    fun test3_Login_CorrectPassword() {
        val user = registerTestUser()

        val response = loginUser(user.email)
        response.then().assertThat().statusCode(HttpStatus.OK.value())
        Logger.info(response.statusLine)
    }

    @Test
    fun test4_Login_WrongPassword() {
        val user = registerTestUser()

        val response = loginUser(user.email, password = "XXXX")
        response.then().assertThat().statusCode(HttpStatus.UNAUTHORIZED.value())
        Logger.info(response.statusLine)
    }

}