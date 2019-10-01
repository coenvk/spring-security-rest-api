package com.arman.huisapp.api.service

import com.arman.huisapp.api.repository.GroupRepository
import com.arman.huisapp.api.repository.UserRepository
import com.arman.huisapp.common.model.Group
import com.arman.huisapp.common.model.request.group.CreateRequest
import org.mindrot.jbcrypt.BCrypt
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import javax.annotation.PostConstruct

@Service
class GroupService(
        @Autowired
        val groupRepository: GroupRepository,
        @Autowired
        val userRepository: UserRepository
) {

    companion object {

        private val Logger = LoggerFactory.getLogger(GroupService::class.java)

    }

    @PostConstruct
    fun init() {
        Logger.info("Constructed ${javaClass.name}")
        Logger.info(groupRepository.toString())
    }

    fun create(request: CreateRequest): Group? {
        if (request.name != null && request.password != null && request.admin != null) {
            return userRepository.findById(request.admin!!).map {
                val group = Group(
                        name = request.name!!,
                        password = BCrypt.hashpw(request.password, BCrypt.gensalt()),
                        admin = it
                )
                groupRepository.save(group)
            }.orElseThrow { Exception() } // TODO: exception class.
        }
        return null
    }

}