package com.get_offer.product.domain

import com.fasterxml.jackson.databind.ObjectMapper
import jakarta.persistence.AttributeConverter
import jakarta.persistence.Converter

@Converter
class ProductImagesConverter : AttributeConverter<ProductImagesVo, String> {
    private val objectMapper = ObjectMapper()

    override fun convertToDatabaseColumn(attribute: ProductImagesVo?): String {
        return objectMapper.writeValueAsString(attribute)
    }

    override fun convertToEntityAttribute(dbData: String?): ProductImagesVo {
        return objectMapper.readValue(dbData, ProductImagesVo::class.java)
    }
}
