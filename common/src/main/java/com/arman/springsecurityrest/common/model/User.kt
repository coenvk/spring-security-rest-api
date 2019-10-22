package com.arman.springsecurityrest.common.model

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonRootName
import org.slf4j.LoggerFactory
import java.time.LocalDate
import javax.annotation.PostConstruct
import javax.persistence.*

@Entity
@Table(name = "users", schema = "public")
@JsonRootName("user")
data class User(
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        @Column(updatable = false, nullable = false, unique = true, insertable = false)
        var id: Long = 0,
        @Column(nullable = false)
        var name: String = "",
        @Column(nullable = false, unique = true)
        var email: String = "",
        @JsonIgnore
        @Column(name = "password_hash", nullable = false)
        var password: String = "",
        @JsonIgnore
        @Column(nullable = true)
        var birthday: LocalDate? = null,
        @JsonIgnore
        @Column(nullable = false, insertable = false)
        var budget: Float = 0f,
        @JsonIgnore
        @Column(nullable = false)
        var enabled: Boolean = false
) : Audit() {

    companion object {

        private val Logger = LoggerFactory.getLogger(User::class.java)

    }

    @ManyToMany(targetEntity = Role::class, fetch = FetchType.EAGER)
    @JoinTable(
            name = "user_roles",
            joinColumns = [JoinColumn(name = "user_id")],
            inverseJoinColumns = [JoinColumn(name = "role_id")]
    )
    @JsonIgnore
    val roles: MutableSet<Role> = hashSetOf()

    @PostConstruct
    fun init() {
        Logger.info("Constructed ${javaClass.name}")
    }

    fun addRole(role: Role) {
        this.roles.add(role)
        role.users.add(this)
    }

    fun removeRole(role: Role) {
        this.roles.remove(role)
        role.users.remove(this)
    }

    override fun toString(): String = "user: { id: $id, name: $name, email: $email }"

    @DslMarker
    private annotation class UserBuilder

    @UserBuilder
    data class Builder(
            var id: Long = 0,
            var name: String = "",
            var email: String = "",
            var password: String = "",
            var birthday: LocalDate? = null,
            var budget: Float = 0f,
            var enabled: Boolean = false
    ) {

        fun id(id: Long) = apply { this.id = id }
        fun name(name: String) = apply { this.name = name }
        fun email(email: String) = apply { this.email = email }
        fun password(password: String) = apply { this.password = password }
        fun birthday(birthday: LocalDate) = apply { this.birthday = birthday }
        fun budget(budget: Float) = apply { this.budget = budget }
        fun enabled(enabled: Boolean) = apply { this.enabled = enabled }

        fun build() = User(id, name, email, password, birthday, budget, enabled)

    }

}

inline fun user(buildUser: User.Builder.() -> Unit): User {
    val builder = User.Builder()
    builder.buildUser()
    return builder.build()
}