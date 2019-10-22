package com.arman.springsecurityrest.common.model.request.user

import com.arman.springsecurityrest.common.model.request.Request
import com.arman.springsecurityrest.common.validation.annotation.Password
import com.fasterxml.jackson.annotation.JsonRootName
import javax.validation.constraints.Email
import javax.validation.constraints.NotEmpty
import javax.validation.constraints.NotNull

@JsonRootName("user")
data class LoginRequest(
        @NotNull
        @NotEmpty
        @Email
        var email: String? = null,
        @NotNull
        @NotEmpty
        @Password
        var password: String? = null
) : Request