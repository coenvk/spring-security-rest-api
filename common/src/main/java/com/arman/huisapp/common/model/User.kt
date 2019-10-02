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

    @DslMarker
    annotation class UserBuilder

    @UserBuilder
    data class Builder(
            var id: Long = 0,
            var name: String = "",
            var email: String = "",
            var password: String = "",
            var birthday: Date? = null,
            var budget: Float = 0f,
            val groups: MutableSet<Group> = mutableSetOf(),
            val adminGroups: MutableSet<Group> = mutableSetOf()
    ) {

        fun id(id: Long) = apply { this.id = id }
        fun name(name: String) = apply { this.name = name }
        fun email(email: String) = apply { this.email = email }
        fun password(password: String) = apply { this.password = password }
        fun birthday(birthday: Date) = apply { this.birthday = birthday }
        fun budget(budget: Float) = apply { this.budget = budget }

        fun group(builder: Group.Builder): Builder = apply {
            this.groups.add(builder.build())
        }

        fun adminGroup(builder: Group.Builder): Builder = apply {
            this.adminGroups.add(builder.build())
        }

        fun build() = User(id, name, email, password, birthday, budget, groups.toSet(), adminGroups.toSet())

    }

}

inline fun user(buildUser: User.Builder.() -> Unit): User {
    val builder = User.Builder()
    builder.buildUser()
    return builder.build()
}

inline fun User.Builder.group(buildGroup: Group.Builder.() -> Unit) {
    val builder = Group.Builder()
    builder.buildGroup()
    group(builder)
}

inline fun User.Builder.adminGroup(buildGroup: Group.Builder.() -> Unit) {
    val builder = Group.Builder()
    builder.buildGroup()
    adminGroup(builder)
}

fun main() {
    user {
        group {
            group {

            }
        }
    }
}