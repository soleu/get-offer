package com.get_offer.product.service

import com.get_offer.common.exception.NotFoundException
import com.get_offer.multipart.ImageService
import com.get_offer.product.controller.ProductPostReqDto
import com.get_offer.product.domain.Product
import com.get_offer.product.domain.ProductImagesVo
import com.get_offer.product.domain.ProductStatus
import com.get_offer.product.repository.ProductRepository
import com.get_offer.user.repository.UserRepository
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit
import org.apache.coyote.BadRequestException
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.multipart.MultipartFile

@Service
@Transactional(readOnly = true)
class ProductService(
    private val imageService: ImageService,
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

    @Transactional
    fun postProduct(req: ProductPostReqDto, userId: Long, images: List<MultipartFile>): ProductSaveDto {

        validateStartPrice(req.startPrice)
        validateDateRange(req.startDate, req.endDate)

        val imageUrls = imageService.saveImages(images)

        val product = productRepository.save(
            Product(
                title = req.title,
                category = req.category,
                writerId = userId,
                images = ProductImagesVo(imageUrls),
                description = req.description,
                startPrice = req.startPrice,
                currentPrice = req.startPrice,
                startDate = req.startDate,
                endDate = req.endDate,
                status = checkStatus(req.startDate)
            )
        )
        return ProductSaveDto.of(product)
    }

    private fun validateStartPrice(startPrice: Int) {
        if (startPrice < 0) {
            throw BadRequestException("startPrice가 0보다 작을 수 없습니다.")
        }
    }

    private fun validateDateRange(startDate: LocalDateTime, endDate: LocalDateTime) {
        if (startDate.isAfter(endDate)) throw BadRequestException("시작 날짜가 유효하지 않습니다.")
        if (ChronoUnit.DAYS.between(startDate, endDate) > 7) throw BadRequestException("경매 기간은 7일을 넘길 수 없습니다.")
    }

    private fun checkStatus(startDate: LocalDateTime): ProductStatus {
        if (startDate.isAfter(LocalDateTime.now())) {
            return ProductStatus.IN_PROGRESS
        }
        return ProductStatus.WAIT
    }
}