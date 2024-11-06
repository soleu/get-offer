package com.get_offer.product.domain

import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface ProductRepository : JpaRepository<Product, Long> {
    fun findAllByWriterIdOrderByEndDateDesc(writerId: Long): List<Product>

    fun findAllByStatusInOrderByEndDateDesc(statuses: List<ProductStatus>, pageRequest: PageRequest): Page<Product>
}