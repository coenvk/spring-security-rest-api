package com.arman.huisapp.api.repository

import com.arman.huisapp.common.model.Group
import org.springframework.boot.autoconfigure.domain.EntityScan
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface GroupRepository : CrudRepository<Group, Long> {

    fun existsByName(name: String): Boolean
    fun findByName(name: String): Group?

}