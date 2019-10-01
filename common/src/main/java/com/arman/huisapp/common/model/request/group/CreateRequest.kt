package com.arman.huisapp.common.model.request.group

import com.arman.huisapp.common.model.request.Request
import com.fasterxml.jackson.annotation.JsonRootName
import javax.validation.constraints.NotNull
import javax.validation.constraints.Size

@JsonRootName("group")
class CreateRequest(
    @NotNull
    @Size(min = 1)
    var name: String? = "",
    @NotNull
    @Size(min = 1)
    var password: String? = "",
    @NotNull
    var admin: Long? = 0
) : Request