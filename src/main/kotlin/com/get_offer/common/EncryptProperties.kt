package com.get_offer.common

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "jasypt.encryptor")
data class EncryptProperties(
    val bean: String,
    val algorithm: String,
    val poolSize: Int,
    val stringOutputType: String,
    val keyObtentionIterations: Int,
    val password: String
)