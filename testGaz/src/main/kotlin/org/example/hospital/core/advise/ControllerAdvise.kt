package org.example.hospital.core.advise

import org.example.hospital.core.exception.AbstractException
import org.example.hospital.generated.api.model.ErrorMessage
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler
import java.lang.reflect.InvocationTargetException

@ControllerAdvice
class GlobalExceptionHandler : ResponseEntityExceptionHandler() {

    @ExceptionHandler(AbstractException::class)
    fun handleCustomException(ex: AbstractException, model: Model): ResponseEntity<ErrorMessage> {
        val httpStatus = getHttpStatusFromException(ex)
        return ResponseEntity(ErrorMessage(message = ex.message), httpStatus)
    }

    private fun getHttpStatusFromException(ex: Throwable): HttpStatus {
        return try {
            val responseStatusAnnotation = ex.javaClass.getAnnotation(ResponseStatus::class.java)
            responseStatusAnnotation?.value ?: HttpStatus.INTERNAL_SERVER_ERROR
        } catch (e: NoSuchMethodException) {
            HttpStatus.INTERNAL_SERVER_ERROR
        } catch (e: InvocationTargetException) {
            HttpStatus.INTERNAL_SERVER_ERROR
        }
    }
}