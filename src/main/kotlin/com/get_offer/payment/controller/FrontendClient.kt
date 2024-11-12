package com.get_offer.payment.controller

import org.springframework.cloud.openfeign.FeignClient
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody

@FeignClient(value = "frontendClient", url = "http://localhost:8080")
interface FrontendClient {
    @PostMapping("/checkout")
    fun checkout(@RequestBody request: CheckoutRequest): String
}