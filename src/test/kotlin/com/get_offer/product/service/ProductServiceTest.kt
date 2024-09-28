package com.get_offer.product.service

import com.get_offer.product.domain.Category
import com.get_offer.product.domain.Product
import com.get_offer.product.domain.ProductStatus
import com.get_offer.product.repository.ProductRepository
import com.get_offer.user.domain.User
import com.get_offer.user.repository.UserRepository
import java.time.LocalDateTime
import java.util.*
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
    private lateinit var mockUserRepository: UserRepository

    @BeforeEach
    fun setUp() {
        mockProductRepository = mock(ProductRepository::class.java)
        mockUserRepository = mock(UserRepository::class.java)
        productService = ProductService(mockProductRepository, mockUserRepository)
    }

    @Test
    @DisplayName("상품 리스트 Dto 변환")
    fun productListToDto() {
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

        val givenProduct2 = Product(
            2L,
            "product2",
            Category.SPORTS,
            "https://image3.png;https://image4.png",
            "desc for product",
            12000,
            15500,
            ProductStatus.WAIT,
            LocalDateTime.now(),
            LocalDateTime.now()
        )

        `when`(mockProductRepository.findAllByStatusInOrderByEndDateDesc(any())).thenReturn(
            listOf(
                givenProduct,
                givenProduct2
            )
        )

        // when
        val sut = productService.getProductList(1L)

        // then
        Assertions.assertThat(sut[0].name).isEqualTo("product")
        Assertions.assertThat(sut[1].name).isEqualTo("product2")
        Assertions.assertThat(sut[0].thumbNail).isEqualTo("https://image1.png")
        Assertions.assertThat(sut[1].thumbNail).isEqualTo("https://image3.png")
        Assertions.assertThat(sut[0].status).isEqualTo(ProductStatus.IN_PROGRESS)
        Assertions.assertThat(sut[1].status).isEqualTo(ProductStatus.WAIT)
        Assertions.assertThat(sut[0].isMine).isEqualTo(true)
        Assertions.assertThat(sut[1].isMine).isEqualTo(false)
    }

    @Test
    @DisplayName("DB에서 이미지리스트를 잘라가져온다.")
    fun productDetailImageSplitToList() {
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

        val givenUser = User(
            "user", "https://image1.png"
        )

        `when`(mockProductRepository.findById(any())).thenReturn(Optional.of(givenProduct))
        `when`(mockUserRepository.findById(any())).thenReturn(Optional.of(givenUser))

        // when
        val sut = productService.getProductDetail(1L, 1L)

        // then
        Assertions.assertThat(sut.images.size).isEqualTo(2)
        Assertions.assertThat(sut.images[0]).isEqualTo("https://image1.png")
        Assertions.assertThat(sut.images[1]).isEqualTo("https://image2.png")
    }
}