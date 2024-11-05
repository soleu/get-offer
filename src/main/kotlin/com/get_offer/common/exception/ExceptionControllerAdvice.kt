package com.get_offer.common.exception

import ApiResponse
import org.apache.coyote.BadRequestException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class ExceptionControllerAdvice {
    @ExceptionHandler
    fun handleIllegalStateException(ex: IllegalStateException): ResponseEntity<ApiResponse<Any>> {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ApiResponse.error(ex.message ?: "DEFAULT ERROR"))
    }

    @ExceptionHandler
    fun handleBadRequestException(ex: BadRequestException): ResponseEntity<ApiResponse<Any>> {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ApiResponse.error(ex.message ?: "DEFAULT ERROR"))
    }

    @ExceptionHandler
    fun handleCustomException(ex: CustomException): ResponseEntity<ApiResponse<Any>> {
        return ResponseEntity.status(ex.code.httpStatus).body(ApiResponse.error(ex.code.message))
    }
}