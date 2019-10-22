package com.arman.springsecurityrest.api.repository

import com.arman.springsecurityrest.common.model.Device
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import javax.transaction.Transactional

@Repository
interface DeviceRepository : CrudRepository<Device, Long> {

    fun findByUserId(userId: Long): MutableIterable<Device>

    @Transactional
    @Modifying
    @Query(nativeQuery = true, value = "ALTER SEQUENCE devices_id_seq RESTART WITH 1;")
    fun resetIdSequence()

}