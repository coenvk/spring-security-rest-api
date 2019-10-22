package com.arman.springsecurityrest.common.model

import com.fasterxml.jackson.annotation.JsonIgnore
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.io.Serializable
import java.time.Instant
import javax.persistence.*

@MappedSuperclass
@EntityListeners(AuditingEntityListener::class)
abstract class Audit : Serializable {

    @Column(name = "created_at", updatable = false, nullable = false, insertable = false)
    @CreatedDate
    @JsonIgnore
    lateinit var createdAt: Instant
        private set

}