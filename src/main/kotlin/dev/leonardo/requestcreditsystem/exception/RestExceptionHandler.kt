package dev.leonardo.requestcreditsystem.exception

import org.springframework.dao.DataAccessException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.validation.FieldError
import org.springframework.validation.ObjectError
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import java.time.LocalDateTime

@RestControllerAdvice
class RestExceptionHandler {
    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun handleValidException(exception: MethodArgumentNotValidException): ResponseEntity<ExceptionDetails> {
        val errors: MutableMap<String, String?> = HashMap()
        exception.bindingResult.allErrors.stream().forEach { err: ObjectError ->
            val fieldName: String = (err as FieldError).field
            val errorMessage: String? = err.defaultMessage
            errors[fieldName] = errorMessage
        }
        return ResponseEntity(
            ExceptionDetails(
                title = "Bad Request!, consult the documentation",
                timestamp = LocalDateTime.now(),
                status = HttpStatus.BAD_REQUEST.value(),
                exception = exception.javaClass
                    .toString()
                    .replace("org.springframework.web.bind.", ""),
                detail = errors
            ),
            HttpStatus.BAD_REQUEST
        )
    }
    @ExceptionHandler(DataAccessException::class)
    fun handleDataAccessException(exception: DataAccessException): ResponseEntity<ExceptionDetails> {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(ExceptionDetails(
            title = "Conflict!, consult the documentation",
            timestamp = LocalDateTime.now(),
            status = HttpStatus.CONFLICT.value(),
            exception = exception.javaClass
                .toString()
                .replace("org.springframework.web.bind.", ""),
            detail = mutableMapOf(exception.cause.toString() to exception.message)
        ))
    }

    @ExceptionHandler(BusinessException::class)
    fun handleBusinessException(exception: BusinessException): ResponseEntity<ExceptionDetails> {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ExceptionDetails(
            title = "Bad Request!, consult the documentation",
            timestamp = LocalDateTime.now(),
            status = HttpStatus.BAD_REQUEST.value(),
            exception = exception.javaClass
                .toString()
                .replace("dev.leonardo.requestcreditsystem.exception.", ""),
            detail = mutableMapOf(exception.cause.toString() to exception.message)
        ))
    }
    @ExceptionHandler(IllegalArgumentException::class)
    fun handleIllegalArgumentException(exception: IllegalArgumentException): ResponseEntity<ExceptionDetails> {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ExceptionDetails(
            title = "Bad Request!, consult the documentation",
            timestamp = LocalDateTime.now(),
            status = HttpStatus.BAD_REQUEST.value(),
            exception = exception.javaClass
                .toString()
                .replace("org.springframework.web.bind.", ""),
            detail = mutableMapOf(exception.cause.toString() to exception.message)
        ))
    }
}
