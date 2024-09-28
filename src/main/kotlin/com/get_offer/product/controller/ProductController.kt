package com.get_offer.product.controller

import ApiResponse
import com.get_offer.product.service.ProductListDto
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
    @GetMapping
    fun getActiveProductList(@RequestParam userId: String): ApiResponse<List<ProductListDto>> {
        return ApiResponse.success(productService.getActiveProductList(userId.toLong()))
    }
}