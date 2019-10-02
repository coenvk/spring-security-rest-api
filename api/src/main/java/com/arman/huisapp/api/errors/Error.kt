package com.arman.huisapp.api.errors

import com.fasterxml.jackson.annotation.JsonFormat
import com.fasterxml.jackson.annotation.JsonRootName
import org.springframework.http.HttpStatus
import java.time.LocalDateTime

@JsonRootName("error")
data class Error(
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss.SSS")
        val timestamp: LocalDateTime = LocalDateTime.now(),
        val status: Int,
        val error: String,
        val message: String,
        val exception: String
) {

    constructor(status: HttpStatus, e: Throwable) : this(
            status,
            "An unexpected error occurred.",
            e
    )

    constructor(status: HttpStatus, message: String, e: Throwable) : this(
            status = status.value(),
            error = status.reasonPhrase,
            message = message,
            exception = e.localizedMessage
    )

}