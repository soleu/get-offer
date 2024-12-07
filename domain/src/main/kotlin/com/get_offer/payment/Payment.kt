package com.get_offer.payment

import com.get_offer.common.AuditingTimeEntity
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table

@Entity
@Table(name = "PAYMENTS")
class Payment(
    // payments 관련 정보 저장
    val userId: Long,

    val auctionId: String, // 추후에 uuid도 검토

    val paymentId: String,

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0L,
) : AuditingTimeEntity()