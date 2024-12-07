package com.get_offer.common.naver

import org.springframework.cloud.openfeign.FeignClient
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestParam

@FeignClient(value = "naverCloudClient", url = "\${naver-cloud.base-url}")
interface NaverCloudClient {
    @PostMapping("/testapp/v1/api-tools/summarization/v2/{appId}")
    fun summary(
        @RequestParam("appId") appId: String,
        @RequestHeader("X-NCP-CLOVASTUDIO-API-KEY") apiKey: String,
        @RequestHeader("X-NCP-APIGW-API-KEY") gwApiKey: String,
        @RequestHeader("Content-Type:") contentType: String,
        @RequestBody request: SummaryReqDto,
    ): SummaryResDto
}