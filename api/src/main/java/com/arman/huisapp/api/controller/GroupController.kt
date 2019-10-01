package com.arman.huisapp.api.controller

import com.arman.huisapp.common.model.Group
import com.arman.huisapp.common.model.request.group.CreateRequest
import com.arman.huisapp.api.service.GroupService
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import org.springframework.validation.Errors
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RestController
import retrofit2.http.FormUrlEncoded
import javax.annotation.PostConstruct
import javax.validation.Valid

@RestController
class GroupController {

    companion object {

        private val Logger = LoggerFactory.getLogger(GroupController::class.java)

    }

    @PostConstruct
    fun init() {
        Logger.info("Constructed ${javaClass.name}")
        Logger.info(service.toString())
    }

    @Autowired
    private lateinit var service: GroupService

    @FormUrlEncoded
    @PostMapping(
        value = ["/api/groups"],
        consumes = [MediaType.APPLICATION_FORM_URLENCODED_VALUE],
        produces = [MediaType.APPLICATION_JSON_VALUE]
    )
    fun create(@Valid request: CreateRequest, errors: Errors) = service.create(request)

    @GetMapping(
        value = ["/api/groups"],
        produces = [MediaType.APPLICATION_JSON_VALUE]
    )
    fun findAll() = service.groupRepository.findAll()

    @GetMapping(
        value = ["/api/groups/{groupId}"],
        produces = [MediaType.APPLICATION_JSON_VALUE]
    )
    fun findById(@Valid @PathVariable groupId: Long): Group {
        return service.groupRepository.findById(groupId).map {
            it
        }.orElseThrow { Exception() }
    }

}