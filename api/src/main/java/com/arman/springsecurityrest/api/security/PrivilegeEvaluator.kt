package com.arman.springsecurityrest.api.security

import org.slf4j.LoggerFactory
import org.springframework.security.access.PermissionEvaluator
import org.springframework.security.core.Authentication
import java.io.Serializable

class PrivilegeEvaluator : PermissionEvaluator {

    companion object {

        private val Logger = LoggerFactory.getLogger(PrivilegeEvaluator::class.java)

    }

    override fun hasPermission(auth: Authentication?, targetDomainObject: Any?, permission: Any?): Boolean {
        if (auth == null || targetDomainObject == null || permission !is String) {
            return false
        }
        val targetType = targetDomainObject.javaClass.simpleName.toUpperCase()
        return hasPrivilege(auth, targetType, permission.toString().toUpperCase())
    }

    override fun hasPermission(auth: Authentication?, targetId: Serializable?, targetType: String?, permission: Any?): Boolean {
        if (auth == null || targetType == null || permission !is String) {
            return false
        }
        return hasPrivilege(auth, targetType.toUpperCase(), permission.toString().toUpperCase())
    }

    private fun hasPrivilege(auth: Authentication, targetType: String, permission: String): Boolean {
        return auth.authorities.any {
            it.authority.startsWith(targetType) && it.authority.contains(permission)
        }
    }

}