package com.get_offer

import com.get_offer.common.jwt.JwtProperties
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.runApplication

@SpringBootApplication
@EnableConfigurationProperties(JwtProperties::class)
class GetOfferApplication

fun main(args: Array<String>) {
    runApplication<GetOfferApplication>(*args)
}
