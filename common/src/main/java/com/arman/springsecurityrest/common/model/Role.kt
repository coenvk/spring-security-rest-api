package com.arman.springsecurityrest.common.model

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonRootName
import org.slf4j.LoggerFactory
import javax.annotation.PostConstruct
import javax.persistence.*

@Entity
@Table(name = "roles", schema = "public")
@JsonRootName("role")
data class Role(
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        @Column(updatable = false, nullable = false, unique = true, insertable = false)
        var id: Long = 0,
        @Column(updatable = false, nullable = false, unique = true, insertable = false)
        var name: String = ""
) {

    companion object {

        private val Logger = LoggerFactory.getLogger(Role::class.java)

    }

    @ManyToMany(targetEntity = User::class, mappedBy = "roles")
    @JsonIgnore
    val users: MutableSet<User> = hashSetOf()
    @ManyToMany(targetEntity = Privilege::class, fetch = FetchType.EAGER)
    @JoinTable(
            name = "role_privileges",
            joinColumns = [JoinColumn(name = "role_id")],
            inverseJoinColumns = [JoinColumn(name = "privilege_id")]
    )
    @JsonIgnore
    val privileges: MutableSet<Privilege> = hashSetOf()

    @PostConstruct
    fun init() {
        Logger.info("Constructed ${javaClass.name}")
    }

    fun addUser(user: User) {
        this.users.add(user)
        user.roles.add(this)
    }

    fun removeUser(user: User) {
        this.users.remove(user)
        user.roles.remove(this)
    }

    fun addPrivilege(privilege: Privilege) {
        this.privileges.add(privilege)
        privilege.roles.add(this)
    }

    fun removePrivilege(privilege: Privilege) {
        this.privileges.remove(privilege)
        privilege.roles.remove(this)
    }

    override fun toString(): String = "role: { id: $id, name: $name }"

    @DslMarker
    private annotation class RoleBuilder

    @RoleBuilder
    data class Builder(
            var id: Long = 0,
            var name: String = ""
    ) {

        fun id(id: Long) = apply { this.id = id }
        fun name(name: String) = apply { this.name = name }

        fun build() = Role(id, name)

    }

}

inline fun role(buildRole: Role.Builder.() -> Unit): Role {
    val builder = Role.Builder()
    builder.buildRole()
    return builder.build()
}