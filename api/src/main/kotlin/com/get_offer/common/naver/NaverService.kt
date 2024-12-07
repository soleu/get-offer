package com.get_offer.common.naver

import org.springframework.stereotype.Service

@Service
class NaverService(
    private val naverCloudClient: NaverCloudClient,
    private val naverCloudProperties: NaverCloudProperties,
) {
    fun summary(text: String): SummaryResDto {
        return naverCloudClient.summary(
            appId = naverCloudProperties.appId,
            apiKey = naverCloudProperties.apiKey,
            gwApiKey = naverCloudProperties.gwApiKey,
            contentType = naverCloudProperties.contentType,
            request = SummaryReqDto.of(text),
        )
    }
}