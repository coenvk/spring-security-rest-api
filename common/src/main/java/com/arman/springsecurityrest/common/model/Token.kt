package com.arman.springsecurityrest.common.model

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonRootName
import java.time.LocalDate
import javax.persistence.*

@Entity
@Table(name = "tokens", schema = "public")
@JsonRootName("token")
data class Token(
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        @Column(updatable = false, nullable = false, unique = true, insertable = false)
        var id: Long = 0,
        @Column(nullable = false)
        @JsonIgnore
        var token: String = "",
        @OneToOne(targetEntity = User::class, fetch = FetchType.EAGER)
        @JoinColumn(nullable = false, name = "user_id")
        @JsonIgnore
        var user: User? = null,
        @Column(name = "expiry_date", updatable = false, nullable = false)
        @JsonIgnore
        var expiryDate: LocalDate? = null,
        @Column(name = "token_type", updatable = false, nullable = false)
        @JsonIgnore
        var tokenType: String = ""
) {

    override fun toString(): String = "token: { id: $id, token: $token, type: $tokenType, expiryDate: $expiryDate }"

    @DslMarker
    private annotation class TokenBuilder

    @TokenBuilder
    data class Builder(
            var id: Long = 0,
            var token: String = "",
            var user: User? = null,
            var expiryDate: LocalDate? = null,
            var tokenType: String = ""
    ) {

        fun id(id: Long) = apply { this.id = id }
        fun token(token: String) = apply { this.token = token }
        fun user(user: User) = apply { this.user = user }
        fun expiryDate(expiryDate: LocalDate) = apply { this.expiryDate = expiryDate }
        fun tokenType(tokenType: String) = apply { this.tokenType = tokenType }

        fun build() = Token(id, token, user, expiryDate, tokenType)

    }

}

inline fun token(buildToken: Token.Builder.() -> Unit): Token {
    val builder = Token.Builder()
    builder.buildToken()
    return builder.build()
}