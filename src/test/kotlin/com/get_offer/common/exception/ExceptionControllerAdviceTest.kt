package com.get_offer.common.exception

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class ExceptionControllerAdviceTest {
    @Test
    fun customExceptionWorksWell() {
        val exception = assertThrows<ApiException> {
            throw ApiException(ExceptionCode.NOTFOUND, "product not exists.")
        }
        assert(exception.message == "product not exists.")
    }
}

