package com.arman.springsecurityrest.common.model.request.user

import com.fasterxml.jackson.annotation.JsonRootName
import javax.validation.constraints.Email
import javax.validation.constraints.NotEmpty
import javax.validation.constraints.NotNull

@JsonRootName("user")
data class ResetPasswordRequest(
        @NotNull
        @NotEmpty
        @Email
        var email: String? = null
)