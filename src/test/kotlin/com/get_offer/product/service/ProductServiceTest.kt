package com.get_offer.product.service

import com.get_offer.product.domain.Category
import com.get_offer.product.domain.Product
import com.get_offer.product.domain.ProductStatus
import com.get_offer.product.repository.ProductRepository
import java.time.LocalDateTime
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.mockito.Mockito.any
import org.mockito.Mockito.mock
import org.mockito.Mockito.`when`

class ProductServiceTest {
    private lateinit var productService: ProductService
    private lateinit var mockProductRepository: ProductRepository

    @BeforeEach
    fun setUp() {
        mockProductRepository = mock(ProductRepository::class.java)
        productService = ProductService(mockProductRepository)
    }

    @Test
    @DisplayName("DB에서 이미지리스트를 잘라가져온다.")
    fun productListImageSplitToList() {
        // given
        val givenProduct = Product(
            1L,
            "product",
            Category.GAMES,
            "https://image1.png;https://image2.png",
            "desc for product",
            10000,
            15000,
            ProductStatus.IN_PROGRESS,
            LocalDateTime.now(),
            LocalDateTime.now()
        )

        `when`(mockProductRepository.findAllByStatusInOrderByEndDateDesc(any())).thenReturn(listOf(givenProduct))

        // when
        val sut = productService.getActiveProductList(2L)

        // then
        Assertions.assertThat(sut.size).isEqualTo(1)
        Assertions.assertThat(sut[0].images.size).isEqualTo(2)
        Assertions.assertThat(sut[0].images[0]).isEqualTo("https://image1.png")
        Assertions.assertThat(sut[0].images[1]).isEqualTo("https://image2.png")
    }
}