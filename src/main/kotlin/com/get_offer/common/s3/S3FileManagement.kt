package com.get_offer.common.s3

import com.get_offer.multipart.FileValidate
import java.util.*
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import org.springframework.web.multipart.MultipartFile
import software.amazon.awssdk.core.sync.RequestBody
import software.amazon.awssdk.services.s3.S3Client
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest
import software.amazon.awssdk.services.s3.model.PutObjectRequest

@Component
class S3FileManagement(
    @Value("\${aws.s3.bucket}")
    private val bucket: String,
    private val amazonS3: S3Client,
) {
    companion object {
        const val TYPE_IMAGE = "image"
    }

    fun uploadImages(multipartFiles: List<MultipartFile>): List<String> {
        return multipartFiles.map { uploadImage(it) }
    }

    fun uploadImage(multipartFile: MultipartFile): String {
        val originalFilename = multipartFile.originalFilename
            ?: throw IllegalStateException()
        FileValidate.checkImageFormat(originalFilename)
        val fileName = "${UUID.randomUUID()}-${originalFilename}"

        val putObjectRequest = PutObjectRequest.builder()
            .bucket(bucket)
            .key(fileName)
            .contentType("/${TYPE_IMAGE}/${getFileExtension(getFileExtension(originalFilename))}")
            .contentLength(multipartFile.inputStream.available().toLong())
            .build()

        amazonS3.putObject(putObjectRequest, RequestBody.fromBytes(multipartFile.bytes))
        return fileName
    }

    fun delete(fileNames: List<String>) {
        fileNames.forEach { delete(it) }
    }

    fun delete(fileName: String) {
        val deleteObjectRequest = DeleteObjectRequest.builder()
            .bucket(bucket)
            .key(fileName)
            .build()
        amazonS3.deleteObject(deleteObjectRequest)
    }

    private fun getFileExtension(fileName: String): String {
        val extensionIndex = fileName.lastIndexOf('.')
        return fileName.substring(extensionIndex + 1)
    }
}