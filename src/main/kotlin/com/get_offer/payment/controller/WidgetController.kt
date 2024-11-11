package com.get_offer.payment.controller

import com.fasterxml.jackson.databind.ObjectMapper
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import java.nio.charset.StandardCharsets
import java.util.*
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestParam

@Controller
class WidgetController {

    private val logger: Logger = LoggerFactory.getLogger(this::class.java)
    private val objectMapper = ObjectMapper() // ObjectMapper 인스턴스 생성

    @PostMapping("/confirm")
    fun confirmPayment(@RequestBody jsonBody: String): ResponseEntity<Map<String, Any>> {
        val requestData: Map<String, Any> = try {
            objectMapper.readValue(jsonBody, Map::class.java) as Map<String, Any>
        } catch (e: Exception) {
            throw RuntimeException(e)
        }

        val paymentKey = requestData["paymentKey"] as? String
        val orderId = requestData["orderId"] as? String
        val amount = requestData["amount"] as? String

        val requestMap = mapOf(
            "orderId" to orderId,
            "amount" to amount,
            "paymentKey" to paymentKey
        )

        val widgetSecretKey = "test_gsk_docs_OaPz8L5KdmQXkzRz3y47BMw6"
        val authorizations =
            "Basic " + Base64.getEncoder().encodeToString("$widgetSecretKey:".toByteArray(StandardCharsets.UTF_8))

        val url = URL("https://api.tosspayments.com/v1/payments/confirm")
        val connection = (url.openConnection() as HttpURLConnection).apply {
            setRequestProperty("Authorization", authorizations)
            setRequestProperty("Content-Type", "application/json")
            requestMethod = "POST"
            doOutput = true
        }

        connection.outputStream.use { outputStream ->
            outputStream.write(objectMapper.writeValueAsBytes(requestMap))
        }

        val code = connection.responseCode
        val responseStream = if (code == 200) connection.inputStream else connection.errorStream

        val responseMap = responseStream.use { stream ->
            InputStreamReader(stream, StandardCharsets.UTF_8).use { reader ->
                objectMapper.readValue(reader, Map::class.java) as Map<String, Any>
            }
        }

        return ResponseEntity.status(code).body(responseMap)
    }

    @GetMapping("/checkout")
    fun checkout(): String {
        return "checkout"
    }

    @GetMapping("/success")
    fun successPayment(
        @RequestParam paymentType: String,
        @RequestParam orderId: String,
        @RequestParam paymentKey: String,
        @RequestParam amount: Number
    ): String {
        return "success"
    }

    @GetMapping("/fail")
    fun failPayment(): String {
        return "fail"
    }
}
