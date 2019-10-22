package com.arman.springsecurityrest.web

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration
import org.springframework.boot.runApplication
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer
import org.springframework.context.annotation.Bean
import org.springframework.web.context.request.RequestContextListener

@SpringBootApplication(exclude = [DataSourceAutoConfiguration::class, HibernateJpaAutoConfiguration::class])
open class WebApplication : SpringBootServletInitializer() {

    @Bean
    open fun requestContextListener(): RequestContextListener {
        return RequestContextListener()
    }

}

fun main(args: Array<String>) {
    runApplication<WebApplication>(*args)
}