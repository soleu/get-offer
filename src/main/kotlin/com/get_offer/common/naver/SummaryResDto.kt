package com.get_offer.common.naver

data class SummaryResDto(
    val status: StatusDto,
    val result: ResultDto,
)

data class StatusDto(
    val code: String,
    val message: String,
)

data class ResultDto(
    val text: String, // 요약 결과
    val inputTokens: Int, // 요약할 문장 입력시 사용한 토큰 수
)