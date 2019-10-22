package com.arman.springsecurityrest.api.error.exception

import java.lang.Exception

class InvalidTokenException(message: String?) : Exception(message)