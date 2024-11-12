package com.get_offer.product.controller

import ApiResponse
import com.get_offer.login.AuthenticatedUser
import com.get_offer.product.service.ProductDetailDto
import com.get_offer.product.service.ProductEditDto
import com.get_offer.product.service.ProductListDto
import com.get_offer.product.service.ProductSaveDto
import com.get_offer.product.service.ProductService
import org.springframework.data.domain.PageRequest
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RequestPart
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.multipart.MultipartFile

@RestController
@RequestMapping("/products")
class ProductController(
    private val productService: ProductService,
) {
    @GetMapping
    fun getProductList(
        @AuthenticatedUser userId: Long, @RequestParam page: Int, @RequestParam size: Int
    ): ApiResponse<PageResponse<ProductListDto>> {
        return ApiResponse.success(
            PageResponse.from(
                productService.getProductList(
                    userId, PageRequest.of(page, size)
                )
            )
        )
    }

    @GetMapping("{id}/detail")
    fun getProductDetail(@PathVariable id: String, @AuthenticatedUser userId: Long): ApiResponse<ProductDetailDto> {
        return ApiResponse.success(productService.getProductDetail(id.toLong(), userId))
    }

    @PostMapping
    fun postProduct(
        @AuthenticatedUser userId: Long,
        @RequestPart("images") images: List<MultipartFile>,
        @RequestPart productReqDto: ProductPostReqDto
    ): ApiResponse<ProductSaveDto> {
        return ApiResponse.success(productService.postProduct(productReqDto, userId, images))
    }

    @PutMapping("{productId}")
    fun editProduct(
        @PathVariable productId: Long,
        @AuthenticatedUser userId: Long,
        @RequestPart("images") images: List<MultipartFile>?,
        @RequestPart productReqDto: ProductEditReqDto
    ): ApiResponse<ProductSaveDto> {
        return ApiResponse.success(
            productService.editProduct(
                ProductEditDto.of(productId, productReqDto, userId, images)
            )
        )
    }
}