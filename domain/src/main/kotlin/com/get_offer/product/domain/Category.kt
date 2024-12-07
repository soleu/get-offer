package com.get_offer.product.domain

enum class Category(
    private val nameKr: String,
) {
    ELECTRONICS("전자 제품"),
    CLOTHES("의류"),
    COSMETICS("화장품"),
    SPORTS("스포츠용품"),
    GAMES("게임"),
    BOOKS("도서"),
    FURNITURE("가구"),
    ETC("기타")
}