package com.arman.huisapp.common.model

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonRootName
import org.slf4j.LoggerFactory
import java.sql.Date
import javax.annotation.PostConstruct
import javax.persistence.*

@Entity
@Table(name = "users", schema = "public")
@JsonRootName("user")
data class User(
        @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(updatable = false, nullable = false)
    var id: Long = 0,
        @Column(nullable = false)
    var name: String = "",
        @Column(nullable = false)
    var email: String = "",
        @JsonIgnore
    @Column(name = "pwd_hash", nullable = false)
    var password: String = "",
        @Column(name = "birthday", nullable = true)
    var birthday: Date? = null,
        @Column(nullable = false)
    var budget: Float = 0f,
        @ManyToMany(targetEntity = Group::class)
    @JoinColumn
    @JsonIgnore
    val groups: Set<Group> = setOf(),
        @OneToMany(mappedBy = "admin")
    @JsonIgnore
    val adminGroups: Set<Group> = setOf()
) : Audit() {

    companion object {

        private val Logger = LoggerFactory.getLogger(User::class.java)

    }

    @PostConstruct
    fun init() {
        Logger.info("Constructed ${javaClass.name}")
    }

    override fun toString(): String = "user: { id: $id, name: $name, email: $email }"

}