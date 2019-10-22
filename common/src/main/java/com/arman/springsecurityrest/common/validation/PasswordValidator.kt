package com.arman.springsecurityrest.common.validation

import com.arman.springsecurityrest.common.validation.annotation.Password
import javax.validation.ConstraintValidator
import javax.validation.ConstraintValidatorContext

class PasswordValidator : ConstraintValidator<Password, String> {

    override fun isValid(password: String?, context: ConstraintValidatorContext): Boolean {
        password?.run {
            if (length < 8 || length > 30
                    || none { c -> c.isUpperCase() }
                    || none { c -> c.isLowerCase() }
                    || none { c -> c.isDigit() }) {
                false
            } else !contains(' ')
        }
        context.disableDefaultConstraintViolation()
        context.buildConstraintViolationWithTemplate("Password constraints were violated.").addConstraintViolation()
        return false
    }

}