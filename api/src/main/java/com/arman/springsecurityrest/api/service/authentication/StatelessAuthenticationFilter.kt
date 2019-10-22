package com.arman.springsecurityrest.api.service.authentication

import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter

class StatelessAuthenticationFilter(private val tokenService: TokenAuthenticationService) : UsernamePasswordAuthenticationFilter() {

//    @Throws(IOException::class, ServletException::class)
//    override fun doFilter(request: ServletRequest?, response: ServletResponse?, filterChain: FilterChain?) {
//        val httpRequest = request as HttpServletRequest
//        SecurityContextHolder.getContext().authentication = tokenService.getToken(httpRequest)
//        filterChain?.doFilter(request, response)
//        SecurityContextHolder.getContext().authentication = null
//    }

}