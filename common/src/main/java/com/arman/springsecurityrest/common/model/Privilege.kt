package com.arman.springsecurityrest.common.model

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonRootName
import org.slf4j.LoggerFactory
import javax.annotation.PostConstruct
import javax.persistence.*

@Entity
@Table(name = "privileges", schema = "public")
@JsonRootName("privilege")
data class Privilege(
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        @Column(updatable = false, nullable = false, unique = true, insertable = false)
        var id: Long = 0,
        @Column(updatable = false, nullable = false, unique = true, insertable = false)
        var name: String = ""
) {

    companion object {

        private val Logger = LoggerFactory.getLogger(Privilege::class.java)

    }

    @ManyToMany(targetEntity = Role::class, mappedBy = "privileges", fetch = FetchType.EAGER)
    @JsonIgnore
    val roles: MutableSet<Role> = hashSetOf()

    @PostConstruct
    fun init() {
        Logger.info("Constructed ${javaClass.name}")
    }

    fun addRole(role: Role) {
        this.roles.add(role)
        role.privileges.add(this)
    }

    fun removeRole(role: Role) {
        this.roles.remove(role)
        role.privileges.remove(this)
    }

    override fun toString(): String = "privilege: { id: $id, name: $name }"

    @DslMarker
    private annotation class PrivilegeBuilder

    @PrivilegeBuilder
    data class Builder(
            var id: Long = 0,
            var name: String = ""
    ) {

        fun id(id: Long) = apply { this.id = id }
        fun name(name: String) = apply { this.name = name }

        fun build() = Privilege(id, name)

    }

}

inline fun privilege(buildPrivilege: Privilege.Builder.() -> Unit): Privilege {
    val builder = Privilege.Builder()
    builder.buildPrivilege()
    return builder.build()
}