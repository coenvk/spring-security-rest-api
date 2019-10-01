package com.arman.huisapp.common.model.request.user

import com.arman.huisapp.common.model.request.Request
import com.fasterxml.jackson.annotation.JsonRootName
import javax.validation.constraints.Email
import javax.validation.constraints.NotNull
import javax.validation.constraints.Size

@JsonRootName("user")
class RegisterRequest(
    @NotNull
    @Size(min = 1)
    var name: String? = "",
    @NotNull
    @Size(min = 1)
    @Email
    var email: String? = "",
    @NotNull
    @Size(min = 1)
    var password: String? = ""
) : Request