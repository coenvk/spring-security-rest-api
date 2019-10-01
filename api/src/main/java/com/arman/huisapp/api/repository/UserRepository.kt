package com.arman.huisapp.api.repository

import com.arman.huisapp.common.model.User
import org.springframework.boot.autoconfigure.domain.EntityScan
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface UserRepository : CrudRepository<User, Long> {

    fun existsByEmail(email: String): Boolean
    fun existsByName(name: String): Boolean
    fun findByEmail(email: String): User?
    fun findByName(name: String): User?

}