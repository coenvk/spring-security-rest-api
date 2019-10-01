package com.arman.huisapp.api.service

import com.arman.huisapp.api.repository.UserRepository
import com.arman.huisapp.common.model.User
import com.arman.huisapp.common.model.request.user.LoginRequest
import com.arman.huisapp.common.model.request.user.RegisterRequest
import org.mindrot.jbcrypt.BCrypt
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import javax.annotation.PostConstruct

@Service
class UserService(
        @Autowired
        val userRepository: UserRepository
) {

    companion object {

        private val Logger = LoggerFactory.getLogger(UserService::class.java)

    }

    @PostConstruct
    fun init() {
        Logger.info("Constructed ${javaClass.name}")
        Logger.info(userRepository.toString())
    }

    val user = ThreadLocal<User>()

    fun login(request: LoginRequest): User? {
        if (request.email != null) {
            userRepository.findByEmail(request.email!!)?.let {
                if (BCrypt.checkpw(request.password, it.password)) {
                    user.set(it)
                    return userRepository.save(it)
                }
            }
            user.remove()
        }
        return null
    }

    fun register(request: RegisterRequest): User? {
        if (request.email != null && request.name != null && request.password != null) {
            val user = User(
                    name = request.name!!,
                    email = request.email!!,
                    password = BCrypt.hashpw(request.password, BCrypt.gensalt())
            )
            userRepository.save(user)
        }
        return null
    }

}