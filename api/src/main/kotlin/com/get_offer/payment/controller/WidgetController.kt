package com.get_offer.payment.controller

import com.fasterxml.jackson.databind.ObjectMapper
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import java.nio.charset.StandardCharsets
import java.util.*
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestParam

@Controller
class WidgetController(
    @Value("\${toss.payments.client_key}") private val clientKey: String,
    @Value("\${toss.payments.secret_key}") private val secretKey: String,
    @Value("\${toss.payments.scriptUrl}") private val scriptUrl: String

) {

    private val logger: Logger = LoggerFactory.getLogger(this::class.java)
    private val objectMapper = ObjectMapper()


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

        val widgetSecretKey = secretKey
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
    fun checkout(
        @RequestBody req: CheckoutRequest,
        model: Model
    ): String {
        // for test
//        model.addAttribute("userId", "MusDYSpBVRRY6xr_s07r3")
//        model.addAttribute("email", "dlthf555@gmail.com")
//        model.addAttribute("username", "이솔")
//        model.addAttribute("phone", "01037352510")
//        model.addAttribute("amount", 15000)
//        model.addAttribute("orderName", "대머리 3종 세트")
//        model.addAttribute("orderId", "Al3xzVz7IC3iHapiJj19Y")

        model.addAttribute("userId", req.userId)
        model.addAttribute("email", req.email)
        model.addAttribute("username", req.username)
        model.addAttribute("phone", req.phone)
        model.addAttribute("amount", req.amount)
        model.addAttribute("orderName", req.orderName)
        model.addAttribute("orderId", req.orderId)
        model.addAttribute("clientKey", clientKey)
        model.addAttribute("scriptUrl", scriptUrl)

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
