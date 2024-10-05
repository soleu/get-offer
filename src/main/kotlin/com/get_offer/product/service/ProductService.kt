package com.get_offer.product.service

import com.get_offer.common.exception.NotFoundException
import com.get_offer.product.domain.Product
import com.get_offer.product.domain.ProductStatus
import com.get_offer.product.repository.ProductRepository
import com.get_offer.user.repository.UserRepository
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.stereotype.Service

@Service
class ProductService(
    private val productRepository: ProductRepository,
    private val userRepository: UserRepository,

    ) {

    fun getProductList(userId: Long, pageRequest: PageRequest): Page<ProductListDto> {
        val productList: Page<Product> = productRepository.findAllByStatusInOrderByEndDateDesc(
            listOf(ProductStatus.IN_PROGRESS, ProductStatus.WAIT), pageRequest
        )
        return productList.map { ProductListDto.of(it, userId) }
    }

    fun getProductDetail(id: Long, userId: Long): ProductDetailDto {
        val product = productRepository.findById(id).orElseThrow { NotFoundException("$id 의 상품은 존재하지 않습니다.") }

        val writer = userRepository.findById(id).orElseThrow { NotFoundException("$id 의 사용자는 존재하지 않습니다.") }

        return ProductDetailDto.of(product, writer, userId)
    }


}