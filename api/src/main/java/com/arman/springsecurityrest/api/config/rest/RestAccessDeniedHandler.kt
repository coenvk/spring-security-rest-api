package com.arman.springsecurityrest.api.config.rest

import org.springframework.http.HttpStatus
import org.springframework.security.access.AccessDeniedException
import org.springframework.security.web.access.AccessDeniedHandler
import org.springframework.stereotype.Component
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Component
class RestAccessDeniedHandler : AccessDeniedHandler {

    override fun handle(request: HttpServletRequest?, response: HttpServletResponse?, e: AccessDeniedException?) {
        response?.let {
            val statusCode = HttpStatus.FORBIDDEN
            response.outputStream.print(statusCode.reasonPhrase)
            response.status = statusCode.value()
        }
    }

}