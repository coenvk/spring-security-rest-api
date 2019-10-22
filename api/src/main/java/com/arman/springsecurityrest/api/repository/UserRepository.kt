package com.arman.springsecurityrest.api.repository

import com.arman.springsecurityrest.common.model.User
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import java.util.*
import javax.transaction.Transactional

@Repository
interface UserRepository : CrudRepository<User, Long> {

    fun existsByEmail(email: String): Boolean
    fun existsByName(name: String): Boolean

    @Transactional
    @Query(nativeQuery = true, value = "SELECT * FROM get_user_by_email(CAST (:email AS email))")
    fun findByEmail(@Param("email") email: String): Optional<User>

    @Transactional
    @Query(nativeQuery = true, value = "SELECT * FROM get_enabled_user_by_email(CAST (:email AS email))")
    fun findByEmailAndEnabled(@Param("email") email: String): Optional<User>

    @Transactional
    @Modifying
    @Query(nativeQuery = true, value = "ALTER SEQUENCE users_id_seq RESTART WITH 1;")
    fun resetIdSequence()

}