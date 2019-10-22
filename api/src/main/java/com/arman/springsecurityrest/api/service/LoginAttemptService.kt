package com.arman.springsecurityrest.api.service

import com.google.common.cache.CacheBuilder
import com.google.common.cache.CacheLoader
import com.google.common.cache.LoadingCache
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.util.concurrent.ExecutionException
import java.util.concurrent.TimeUnit
import javax.annotation.PostConstruct
import javax.servlet.http.HttpServletRequest

@Service
class LoginAttemptService {

    companion object {

        const val MAX_ATTEMPTS = 10

        private val Logger = LoggerFactory.getLogger(LoginAttemptService::class.java)

    }

    @Autowired
    private lateinit var httpRequest: HttpServletRequest

    @PostConstruct
    fun init() {
        Logger.info("Constructed ${javaClass.name}")
    }

    private val attemptsCache: LoadingCache<String, Int> =
            CacheBuilder.newBuilder()
                    .expireAfterWrite(1, TimeUnit.DAYS)
                    .build(object : CacheLoader<String, Int>() {
                        override fun load(key: String): Int {
                            return 0
                        }
                    })

    fun loginFailed(request: HttpServletRequest = httpRequest) {
        val key = findClientIp(request)
        var attempts = 0
        try {
            attempts = attemptsCache.get(key)
        } catch (e: ExecutionException) {
        }
        Logger.warn("User at ip $key failed with ${attempts + 1} attempts.")
        attemptsCache.put(key, attempts + 1)
    }

    fun loginSucceeded(request: HttpServletRequest = httpRequest) {
        val key = findClientIp(request)
        attemptsCache.invalidate(key)
    }

    fun isBlocked(request: HttpServletRequest = httpRequest): Boolean {
        val key = findClientIp(request)
        return try {
            attemptsCache.get(key) >= MAX_ATTEMPTS
        } catch (e: ExecutionException) {
            false
        }
    }

    internal fun reset() {
        attemptsCache.invalidateAll()
    }

    private fun findClientIp(request: HttpServletRequest): String {
        val header = request.getHeader("X-Forwarded-For")
        header?.let {
            return header.split(",")[0]
        }
        return request.remoteAddr
    }

}