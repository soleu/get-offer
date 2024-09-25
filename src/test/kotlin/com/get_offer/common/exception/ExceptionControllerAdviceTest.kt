package com.get_offer.common.exception

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class ExceptionControllerAdviceTest {
    @Test
    fun customExceptionWorksWell() {
        val exception = assertThrows<ProductNotFoundException> {
            throw ProductNotFoundException("product not exists.")
        }
        assert(exception.message == "product not exists.")
    }
}

