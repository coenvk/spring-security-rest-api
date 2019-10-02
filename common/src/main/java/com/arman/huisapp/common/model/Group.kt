package com.arman.huisapp.common.model

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonRootName
import org.slf4j.LoggerFactory
import javax.annotation.PostConstruct
import javax.persistence.*

@Entity
@Table(name = "groups", schema = "public")
@JsonRootName("group")
data class Group(
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        @Column(updatable = false, nullable = false)
        var id: Long = 0,
        @Column(nullable = false)
        var name: String = "",
        @JsonIgnore
        @Column(name = "pwd_hash", nullable = false)
        var password: String = "",
        @ManyToOne(targetEntity = User::class)
        @JoinColumn(name = "admin")
        var admin: User? = null,
        @ManyToMany(targetEntity = User::class, mappedBy = "groups")
        @JsonIgnore
        val users: Set<User> = setOf()
) : Audit() {

    companion object {

        private val Logger = LoggerFactory.getLogger(Group::class.java)

    }

    @PostConstruct
    fun init() {
        Logger.info("Constructed ${javaClass.name}")
    }

    override fun toString(): String = "group: { id: $id, name: $name }"

    @DslMarker
    annotation class GroupBuilder

    @GroupBuilder
    data class Builder(
            var id: Long = 0,
            var name: String = "",
            var password: String = "",
            var admin: User? = null,
            val users: MutableSet<User> = mutableSetOf()
    ) {

        fun id(id: Long) = apply { this.id = id }
        fun name(name: String) = apply { this.name = name }
        fun password(password: String) = apply { this.password = password }
        fun admin(admin: User) = apply { this.admin = admin }

        fun user(builder: User.Builder): Builder = apply {
            this.users.add(builder.build())
        }

        fun build() = Group(id, name, password, admin, users.toSet())

    }

}

inline fun group(buildGroup: Group.Builder.() -> Unit): Group {
    val builder = Group.Builder()
    builder.buildGroup()
    return builder.build()
}

inline fun Group.Builder.user(buildUser: User.Builder.() -> Unit) {
    val builder = User.Builder()
    builder.buildUser()
    user(builder)
}