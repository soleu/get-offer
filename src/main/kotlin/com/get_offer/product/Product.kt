package com.get_offer.product

import com.get_offer.common.AuditingTimeEntity
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import java.time.LocalDateTime

@Entity
class Product(
    @Column(name = "user_id", nullable = false)
    private val userId: Long,

    private val name: String,

    private val category: Category,

    private val images: String,

    private val description: String,

    private val startPrice: Int,

    private var currentPrice: Int,

    private var status: ProductStatus,

    private var startDate: LocalDateTime,

    private var endDate: LocalDateTime
) : AuditingTimeEntity() {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private val id: Long? = null
}