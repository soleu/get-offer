package com.get_offer.common.naver

class SummaryReqDto(
    val texts: List<String>
) {
    companion object {
        fun of(text: String): SummaryReqDto {
            return SummaryReqDto(listOf(text))
        }
    }
}