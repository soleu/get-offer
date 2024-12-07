package com.get_offer.common.exception

import org.springframework.http.HttpStatus

class ApiException(val code: ExceptionCode, override var message: String? = null) : RuntimeException(message) {
    init {
        if (message != null) {
            code.message = message as String
        }
    }
}


enum class ExceptionCode(
    val httpStatus: HttpStatus,
    var message: String,
) {
    NOTFOUND(HttpStatus.NOT_FOUND, "존재하지 않습니다."),
    UN_AUTHORIZED(HttpStatus.UNAUTHORIZED, "인가되지 않은 사용자입니다"),
    UN_SUPPORTED(HttpStatus.UNSUPPORTED_MEDIA_TYPE, "파일의 확장자가 유효하지 않습니다."),
    BADREQUEST(HttpStatus.BAD_REQUEST, "잘못된 요청입니다.")
}