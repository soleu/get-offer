package com.get_offer.payment.service

import com.get_offer.payment.controller.CheckoutReqDto
import org.springframework.cloud.openfeign.FeignClient
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody

@FeignClient(value = "frontendClient", url = "\${spring.baseurl}")
interface FrontendClient {
    @PostMapping("/checkout")
    fun checkout(@RequestBody request: CheckoutReqDto): String
}