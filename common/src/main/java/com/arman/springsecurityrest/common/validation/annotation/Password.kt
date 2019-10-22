package com.arman.springsecurityrest.common.validation.annotation

import com.arman.springsecurityrest.common.validation.PasswordValidator
import javax.validation.Constraint
import javax.validation.Payload
import kotlin.reflect.KClass

@MustBeDocumented
@Constraint(validatedBy = [PasswordValidator::class])
@Target(AnnotationTarget.ANNOTATION_CLASS, AnnotationTarget.TYPE, AnnotationTarget.FIELD)
@Retention(AnnotationRetention.RUNTIME)
annotation class Password(
        val message: String = "Invalid Password",
        val groups: Array<KClass<*>> = [],
        val payload: Array<KClass<Payload>> = []
)