package com.get_offer.product.controller

import com.get_offer.product.service.ActiveProductListDto
import com.get_offer.product.service.ProductService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/products")
class ProductController(
    private val productService: ProductService
) {
    @GetMapping("/activeList")
    fun getActiveProductList(@RequestParam userId: String): List<ActiveProductListDto> {
        return productService.getActiveProductList(userId.toLong())
    }
}