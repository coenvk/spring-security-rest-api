package com.arman.huisapp.api.controller

import com.arman.huisapp.api.service.GroupService
import com.arman.huisapp.common.model.Group
import com.arman.huisapp.common.model.request.group.CreateRequest
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.validation.Errors
import org.springframework.web.bind.annotation.*
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
    @ResponseBody
    fun create(@Valid request: CreateRequest, errors: Errors): ResponseEntity<Group> {
        service.create(request)?.let {
            return ResponseEntity.ok(it)
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null)
    }

    @GetMapping(
            value = ["/api/groups"],
            produces = [MediaType.APPLICATION_JSON_VALUE]
    )
    @ResponseBody
    fun findAll(): ResponseEntity<MutableIterable<Group>> {
        return ResponseEntity.ok(service.groupRepository.findAll())
    }

    @GetMapping(
            value = ["/api/groups/{groupId}"],
            produces = [MediaType.APPLICATION_JSON_VALUE]
    )
    @ResponseBody
    fun findById(@Valid @PathVariable groupId: Long): ResponseEntity<Group> {
        return service.groupRepository.findById(groupId).map {
            ResponseEntity.ok(it)
        }.orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).body(null))
    }

}