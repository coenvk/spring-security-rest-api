package com.arman.springsecurityrest.api.error

import com.arman.springsecurityrest.api.error.exception.InvalidPasswordException
import com.arman.springsecurityrest.api.error.exception.InvalidRequestParameterException
import com.arman.springsecurityrest.api.error.exception.InvalidTokenException
import org.springframework.beans.TypeMismatchException
import org.springframework.core.Ordered
import org.springframework.core.annotation.Order
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.http.converter.HttpMessageNotReadableException
import org.springframework.http.converter.HttpMessageNotWritableException
import org.springframework.web.HttpMediaTypeNotAcceptableException
import org.springframework.web.HttpMediaTypeNotSupportedException
import org.springframework.web.HttpRequestMethodNotSupportedException
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.MissingPathVariableException
import org.springframework.web.bind.MissingServletRequestParameterException
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.context.request.WebRequest
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException
import org.springframework.web.servlet.NoHandlerFoundException
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler
import javax.persistence.EntityExistsException
import javax.persistence.EntityNotFoundException
import javax.validation.ConstraintViolationException

@Order(Ordered.HIGHEST_PRECEDENCE)
@ControllerAdvice
class RestExceptionHandler : ResponseEntityExceptionHandler() {

    override fun handleTypeMismatch(
            e: TypeMismatchException,
            headers: HttpHeaders,
            status: HttpStatus,
            request: WebRequest): ResponseEntity<Any> {
        return buildResponseEntity(Error(status, "A type was mismatched where ${e.requiredType} was required.", e))
    }

    override fun handleHttpMediaTypeNotAcceptable(
            e: HttpMediaTypeNotAcceptableException,
            headers: HttpHeaders,
            status: HttpStatus,
            request: WebRequest): ResponseEntity<Any> {
        return buildResponseEntity(Error(status, "The media type is not accepted.", e))
    }

    override fun handleMissingPathVariable(
            e: MissingPathVariableException,
            headers: HttpHeaders,
            status: HttpStatus,
            request: WebRequest): ResponseEntity<Any> {
        return buildResponseEntity(Error(status, "Path variable ${e.variableName} is missing.", e))
    }

    override fun handleHttpRequestMethodNotSupported(
            e: HttpRequestMethodNotSupportedException,
            headers: HttpHeaders,
            status: HttpStatus,
            request: WebRequest): ResponseEntity<Any> {
        return buildResponseEntity(Error(status, "Request method ${e.method} is not supported.", e))
    }

    override fun handleMissingServletRequestParameter(
            e: MissingServletRequestParameterException,
            headers: HttpHeaders,
            status: HttpStatus,
            request: WebRequest): ResponseEntity<Any> {
        return buildResponseEntity(Error(status, "Parameter ${e.parameterName} is missing.", e))
    }

    override fun handleHttpMediaTypeNotSupported(
            e: HttpMediaTypeNotSupportedException,
            headers: HttpHeaders,
            status: HttpStatus,
            request: WebRequest): ResponseEntity<Any> {
        return buildResponseEntity(Error(status, "Media type ${e.contentType} is not supported. Supported media types are ${e.supportedMediaTypes.toTypedArray().contentToString()}.", e))
    }

    override fun handleMethodArgumentNotValid(
            e: MethodArgumentNotValidException,
            headers: HttpHeaders,
            status: HttpStatus,
            request: WebRequest): ResponseEntity<Any> {
        return buildResponseEntity(Error(status, "A validation error occurred on parameter ${e.parameter}.", e))
    }

    @ExceptionHandler(ConstraintViolationException::class)
    fun handleConstraintViolation(
            e: ConstraintViolationException): ResponseEntity<Any> {
        return buildResponseEntity(Error(HttpStatus.BAD_REQUEST, "These constraints are violated ${e.constraintViolations.toTypedArray().contentToString()}.", e))
    }

    override fun handleHttpMessageNotReadable(
            e: HttpMessageNotReadableException,
            headers: HttpHeaders,
            status: HttpStatus,
            request: WebRequest): ResponseEntity<Any> {
        return buildResponseEntity(Error(status, "Malformed JSON request.", e))
    }

    override fun handleHttpMessageNotWritable(
            e: HttpMessageNotWritableException,
            headers: HttpHeaders,
            status: HttpStatus,
            request: WebRequest): ResponseEntity<Any> {
        return buildResponseEntity(Error(status, "An error occurred while writing JSON output.", e))
    }

    override fun handleNoHandlerFoundException(
            e: NoHandlerFoundException,
            headers: HttpHeaders,
            status: HttpStatus,
            request: WebRequest): ResponseEntity<Any> {
        return buildResponseEntity(Error(status, "Could not find the ${e.httpMethod} method for URL ${e.requestURL}", e))
    }

    @ExceptionHandler(EntityNotFoundException::class)
    fun handleEntityNotFound(
            e: EntityNotFoundException): ResponseEntity<Any> {
        return buildResponseEntity(Error(HttpStatus.NOT_FOUND, e))
    }

    @ExceptionHandler(DataIntegrityViolationException::class)
    fun handleDataIntegrityViolation(
            e: DataIntegrityViolationException): ResponseEntity<Any> {
        return buildResponseEntity(Error(HttpStatus.INTERNAL_SERVER_ERROR, "An error occurred in the database.", e))
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException::class)
    fun handleMethodArgumentTypeMismatch(
            e: MethodArgumentTypeMismatchException): ResponseEntity<Any> {
        return buildResponseEntity(Error(HttpStatus.BAD_REQUEST, "The parameter ${e.name} of value ${e.value} could not be converted to type ${e.requiredType.simpleName}.", e))
    }

    @ExceptionHandler(EntityExistsException::class)
    fun handleEntityExists(
            e: EntityExistsException): ResponseEntity<Any> {
        return buildResponseEntity(Error(HttpStatus.INTERNAL_SERVER_ERROR, e))
    }

    @ExceptionHandler(IllegalArgumentException::class, IllegalStateException::class, InvalidRequestParameterException::class)
    fun handleConflict(
            e: RuntimeException): ResponseEntity<Any> {
        return buildResponseEntity(Error(HttpStatus.CONFLICT, e))
    }

    @ExceptionHandler(InvalidPasswordException::class)
    fun handleInvalidPassword(
            e: InvalidPasswordException): ResponseEntity<Any> {
        return buildResponseEntity(Error(HttpStatus.INTERNAL_SERVER_ERROR, e))
    }

    @ExceptionHandler(InvalidTokenException::class)
    fun handleInvalidToken(
            e: InvalidTokenException): ResponseEntity<Any> {
        return buildResponseEntity(Error(HttpStatus.INTERNAL_SERVER_ERROR, e))
    }

    @ExceptionHandler(Exception::class)
    fun handleDefault(
            e: Exception): ResponseEntity<Any> {
        return buildResponseEntity(Error(HttpStatus.INTERNAL_SERVER_ERROR, e))
    }

    private fun buildResponseEntity(error: Error): ResponseEntity<Any> {
        return ResponseEntity(error, HttpStatus.valueOf(error.status))
    }

}