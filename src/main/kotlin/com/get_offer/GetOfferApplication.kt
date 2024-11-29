package com.get_offer

import com.get_offer.common.EncryptProperties
import com.get_offer.common.naver.NaverCloudProperties
import com.get_offer.login.jwt.JwtProperties
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.runApplication
import org.springframework.cloud.openfeign.EnableFeignClients
import org.springframework.context.annotation.EnableAspectJAutoProxy
import org.springframework.scheduling.annotation.EnableScheduling

@SpringBootApplication
@EnableBatchProcessing
@EnableScheduling
@EnableAspectJAutoProxy
@EnableConfigurationProperties(JwtProperties::class, EncryptProperties::class, NaverCloudProperties::class)
@EnableFeignClients(basePackages = ["com.get_offer.payment.service", "com.get_offer.common.naver"])
class GetOfferApplication

fun main(args: Array<String>) {
    runApplication<GetOfferApplication>(*args)
}
