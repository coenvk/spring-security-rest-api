package com.arman.springsecurityrest.api.config

import com.arman.springsecurityrest.api.config.rest.*
import com.arman.springsecurityrest.api.service.UserService
import com.arman.springsecurityrest.api.service.authentication.StatelessAuthenticationFilter
import com.arman.springsecurityrest.api.service.authentication.TokenAuthenticationService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.security.authentication.AuthenticationProvider
import org.springframework.security.authentication.dao.DaoAuthenticationProvider
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.util.matcher.AntPathRequestMatcher
import org.springframework.web.context.request.RequestContextListener
import javax.sql.DataSource

@Configuration
@EnableWebSecurity
class WebSecurityConfig : WebSecurityConfigurerAdapter() {

    @Autowired
    private lateinit var dataSource: DataSource

    @Autowired
    private lateinit var userDetailsService: UserService

    @Autowired
    private lateinit var successHandler: RestAuthenticationSuccessHandler

    @Autowired
    private lateinit var failureHandler: RestAuthenticationFailureHandler

    @Autowired
    private lateinit var accessDeniedHandler: RestAccessDeniedHandler

    @Autowired
    private lateinit var authenticationEntryPoint: RestAuthenticationEntryPoint

    @Autowired
    private lateinit var logoutSuccessHandler: RestLogoutSuccessHandler

    @Throws(Exception::class)
    override fun configure(auth: AuthenticationManagerBuilder) {
        auth
                .authenticationProvider(authenticationProvider())
                .jdbcAuthentication()
                .dataSource(dataSource)
    }

    @Throws(Exception::class)
    override fun configure(http: HttpSecurity) {
        http
                .csrf().disable()
                .exceptionHandling()
                .accessDeniedHandler(accessDeniedHandler)
                .authenticationEntryPoint(authenticationEntryPoint)
                .and()

//                .headers()
//                .cacheControl()
//                .and()
//                .xssProtection()
//                .and()
//                .and()

                .authorizeRequests()
                .antMatchers(HttpMethod.POST, "/api/users")
                .permitAll()
                .antMatchers("/api/**")
                .authenticated()

                .and()
                .formLogin()
                .loginProcessingUrl("/login")
                .successHandler(successHandler)
                .failureHandler(failureHandler)
                .usernameParameter("email")
                .passwordParameter("password")

//                .and()
//                .addFilterBefore(authenticationFilter(), UsernamePasswordAuthenticationFilter::class.java)

                .and()
                .logout()
                .logoutUrl("/logout")
                .logoutSuccessHandler(logoutSuccessHandler)
                .clearAuthentication(true)
                .invalidateHttpSession(true)
                .deleteCookies("JSESSIONID")
    }

    @Bean
    fun passwordEncoder(): PasswordEncoder {
        return BCryptPasswordEncoder()
    }

    @Bean
    fun authenticationFilter(): StatelessAuthenticationFilter {
        val authenticationService = TokenAuthenticationService("tooManySecrets", userDetailsService)
        val authenticationFilter = StatelessAuthenticationFilter(authenticationService)
        authenticationFilter.setAuthenticationManager(authenticationManagerBean())
        authenticationFilter.setAuthenticationSuccessHandler(successHandler)
        authenticationFilter.setAuthenticationFailureHandler(failureHandler)
        authenticationFilter.setPostOnly(true)
        authenticationFilter.usernameParameter = "email"
        authenticationFilter.passwordParameter = "password"
        authenticationFilter.setRequiresAuthenticationRequestMatcher(AntPathRequestMatcher("/login", "POST"))
        return authenticationFilter
    }

    @Bean
    fun authenticationProvider(): AuthenticationProvider {
        val provider = DaoAuthenticationProvider()
        provider.setUserDetailsService(userDetailsService)
        provider.setPasswordEncoder(passwordEncoder())
        provider.isHideUserNotFoundExceptions = true
        return provider
    }

    @Bean
    fun requestContextListener(): RequestContextListener {
        return RequestContextListener()
    }

}