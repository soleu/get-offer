package com.get_offer.common.exception

import ApiResponse
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler

@ControllerAdvice
class ExceptionControllerAdvice {
    @ExceptionHandler
    fun handleIllegalStateException(ex: IllegalStateException): ApiResponse<Any> {
        return ApiResponse.error(ex.message ?: "DEFAULT ERROR", HttpStatus.BAD_REQUEST)
    }

    @ExceptionHandler
    fun handleNotFoundException(ex: NotFoundException): ApiResponse<Any> {
        return ApiResponse.error(ex.message, HttpStatus.NOT_FOUND)
    }
}