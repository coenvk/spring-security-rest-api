package com.arman.huisapp.api

import org.slf4j.LoggerFactory
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.domain.EntityScan
import org.springframework.boot.runApplication
import org.springframework.context.annotation.ComponentScan
import org.springframework.data.jpa.repository.config.EnableJpaAuditing
import org.springframework.data.jpa.repository.config.EnableJpaRepositories
import javax.annotation.PostConstruct

@SpringBootApplication
@EnableJpaAuditing
@ComponentScan(basePackages = ["com.arman.huisapp"])
@EntityScan(basePackages = ["com.arman.huisapp.common.model"])
@EnableJpaRepositories(basePackages = ["com.arman.huisapp.api.repository"])
class Api : CommandLineRunner {

    companion object {

        private val Logger = LoggerFactory.getLogger(Api::class.java)

    }

    @PostConstruct
    fun init() {
        Logger.info("Constructed ${javaClass.name}")
    }

    override fun run(vararg args: String?) {
        Logger.info("Running ${javaClass.name}")
        for (i in 0 until args.size) {
            Logger.info("args[{}]: {}", i, args[i])
        }
    }

}

fun main(args: Array<String>) {
    runApplication<Api>(*args)
}