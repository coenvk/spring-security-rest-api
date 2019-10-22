package com.arman.springsecurityrest.api.service

import com.arman.springsecurityrest.api.repository.DeviceRepository
import com.arman.springsecurityrest.common.model.Device
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class DeviceService(
        @Autowired
        val deviceRepository: DeviceRepository
) : EntityService<Device> {

    companion object {

        private val Logger = LoggerFactory.getLogger(DeviceService::class.java)

    }

}