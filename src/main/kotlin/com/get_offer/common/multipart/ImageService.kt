package com.get_offer.common.multipart

import com.get_offer.common.s3.S3FileManagement
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile

@Service
class ImageService(
    private val s3FileManagement: S3FileManagement,
) {
    fun saveImages(images: List<MultipartFile>): List<String> {
        return s3FileManagement.uploadImages(images)
    }

    fun saveByteImage(imageBytes: ByteArray, fileName: String): String {
        return s3FileManagement.updateByteImage(imageBytes, fileName)
    }

    fun deleteImages(imageUrls: List<String>) {
        s3FileManagement.delete(imageUrls)
    }
}