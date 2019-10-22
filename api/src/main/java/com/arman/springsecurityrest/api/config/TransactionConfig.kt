package com.arman.springsecurityrest.api.config

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.orm.jpa.JpaTransactionManager
import org.springframework.transaction.PlatformTransactionManager
import org.springframework.transaction.annotation.EnableTransactionManagement
import javax.persistence.EntityManagerFactory
import javax.sql.DataSource

@Configuration
@EnableTransactionManagement
class TransactionConfig {

    @Autowired
    private lateinit var emf: EntityManagerFactory

    @Autowired
    private lateinit var dataSource: DataSource

    @Bean("transactionManager")
    fun transactionManager(): PlatformTransactionManager {
        val tm = JpaTransactionManager()
        tm.entityManagerFactory = emf
        tm.dataSource = dataSource
        return tm
    }

}