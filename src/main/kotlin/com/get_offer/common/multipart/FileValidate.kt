package com.get_offer.common.multipart

import com.get_offer.common.exception.CustomException
import com.get_offer.common.exception.ExceptionCode

class FileValidate {
    companion object {
        private val IMAGE_EXTENSIONS: List<String> = listOf("jpg", "png", "jpeg", "JPG", "PNG", "JPEG")

        fun checkImageFormat(fileName: String) {
            val extensionIndex = fileName.lastIndexOf('.')
            if (extensionIndex == -1) {
                throw CustomException(ExceptionCode.UN_SUPPORTED)
            }
            val extension = fileName.substring(extensionIndex + 1)
            require(IMAGE_EXTENSIONS.contains(extension)) {
                throw CustomException(ExceptionCode.UN_SUPPORTED)
            }
        }
    }
}