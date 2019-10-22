package com.arman.springsecurityrest.api.unittest.user

import com.arman.springsecurityrest.api.unittest.ApiTest
import com.arman.springsecurityrest.common.model.User
import io.restassured.RestAssured
import io.restassured.response.Response

abstract class UserTest : ApiTest() {

    protected companion object {

        const val RegisterUrl = "$ApiUrl/users"
        const val ChangePasswordUrl = "/changePassword"

    }

    protected fun changePassword(email: String, oldPassword: String, password: String): Response {
        return RestAssured.given()
                .header("Content-Type", "application/x-www-form-urlencoded")
                .formParam("email", email)
                .formParam("oldPassword", oldPassword)
                .formParam("password", password)
                .request().post(ChangePasswordUrl)
    }

}