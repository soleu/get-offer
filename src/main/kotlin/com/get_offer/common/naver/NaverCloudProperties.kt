package com.get_offer.common.naver

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "naver-cloud")
data class NaverCloudProperties(
    val appId: String,
    val apiKey: String,
    val gwApiKey: String,
    val contentType: String = "application/json"
)