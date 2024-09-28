package com.get_offer.product.controller

import ApiResponse
import com.get_offer.product.service.ProductDetailDto
import com.get_offer.product.service.ProductListDto
import com.get_offer.product.service.ProductService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/products")
class ProductController(
    private val productService: ProductService
) {
    @GetMapping
    fun getProductList(@RequestParam userId: String): ApiResponse<List<ProductListDto>> {
        return ApiResponse.success(productService.getProductList(userId.toLong()))
    }

    @GetMapping("{id}/detail")
    fun getProductDetail(@PathVariable id: String, @RequestParam userId: String): ApiResponse<ProductDetailDto> {
        return ApiResponse.success(productService.getProductDetail(id.toLong(), userId.toLong()))
    }
}