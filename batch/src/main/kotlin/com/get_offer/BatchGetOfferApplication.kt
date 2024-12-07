package com.get_offer

import com.get_offer.common.EncryptProperties
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.runApplication
import org.springframework.scheduling.annotation.EnableScheduling

@SpringBootApplication
@EnableBatchProcessing
@EnableScheduling
@EnableConfigurationProperties(EncryptProperties::class)
class GetOfferApplication

fun main(args: Array<String>) {
    runApplication<GetOfferApplication>(*args)
}
