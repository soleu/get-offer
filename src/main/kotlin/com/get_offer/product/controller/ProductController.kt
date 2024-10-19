package com.get_offer.product.controller

import ApiResponse
import com.get_offer.multipart.ImageService
import com.get_offer.product.service.ProductDetailDto
import com.get_offer.product.service.ProductListDto
import com.get_offer.product.service.ProductService
import org.springframework.data.domain.PageRequest
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RequestPart
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.multipart.MultipartFile

@RestController
@RequestMapping("/products")
class ProductController(
    private val productService: ProductService,
    private val imageService: ImageService,
) {
    @GetMapping
    fun getProductList(
        @RequestParam userId: String, @RequestParam page: Int, @RequestParam size: Int
    ): ApiResponse<PageResponse<ProductListDto>> {
        return ApiResponse.success(
            PageResponse.from(
                productService.getProductList(
                    userId.toLong(), PageRequest.of(page, size)
                )
            )
        )
    }

    @GetMapping("{id}/detail")
    fun getProductDetail(@PathVariable id: String, @RequestParam userId: String): ApiResponse<ProductDetailDto> {
        return ApiResponse.success(productService.getProductDetail(id.toLong(), userId.toLong()))
    }

    @PostMapping
    fun postProduct(
        @RequestParam userId: String, @RequestPart images: List<MultipartFile>
    ) {
        try {
            imageService.saveImages(images)
        } catch (e: Exception) {
            throw e
        }
    }
}