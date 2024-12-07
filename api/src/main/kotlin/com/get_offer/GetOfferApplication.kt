package com.get_offer

import com.get_offer.common.EncryptProperties
import com.get_offer.common.naver.NaverCloudProperties
import com.get_offer.login.jwt.JwtProperties
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.runApplication
import org.springframework.cloud.openfeign.EnableFeignClients

@SpringBootApplication
@EnableConfigurationProperties(JwtProperties::class, EncryptProperties::class, NaverCloudProperties::class)
@EnableFeignClients(basePackages = ["com.get_offer.payment.service", "com.get_offer.common.naver"])
class GetOfferApplication

fun main(args: Array<String>) {
    runApplication<GetOfferApplication>(*args)
}
