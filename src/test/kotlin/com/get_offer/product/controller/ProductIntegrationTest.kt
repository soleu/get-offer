package com.get_offer.product.controller

import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultHandlers
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@SpringBootTest
@AutoConfigureMockMvc
class ProductIntegrationTest(
    @Autowired val mockMvc: MockMvc,
) {
    @Test
    fun activeProductListIntegrationTest() {
        mockMvc.perform(
            get("/products").param("userId", "1")
        ).andDo(MockMvcResultHandlers.print()).andExpect(status().isOk).andExpect(jsonPath("$.size()").value(1))
            .andExpect(jsonPath("$[0].id").value("1"))
            .andExpect(jsonPath("$[0].writerId").value("1"))
            .andExpect(jsonPath("$[0].name").value("nintendo"))
            .andExpect(jsonPath("$[0].category").value("GAMES"))
            .andExpect(jsonPath("$[0].thumbNail").value("png"))
            .andExpect(jsonPath("$[0].currentPrice").value("10000"))
            .andExpect(jsonPath("$[0].status").value("IN_PROGRESS"))
            .andExpect(jsonPath("$[0].startDate").value("2024-01-02T00:00:00"))
            .andExpect(jsonPath("$[0].endDate").value("2024-01-04T00:00:00"))
            .andExpect(jsonPath("$[0].isMine").value("true"))
    }

    @Test
    fun productDetailIntegrationTest() {
        mockMvc.perform(
            get("/products/1/detail").param("userId", "1")
        ).andDo(MockMvcResultHandlers.print()).andExpect(status().isOk)
            .andExpect(jsonPath("$.id").value("1"))
            .andExpect(jsonPath("$.name").value("nintendo"))
            .andExpect(jsonPath("$.writerId").value("1"))
            .andExpect(jsonPath("$.writerNickname").value("test"))
            .andExpect(jsonPath("$.writerProfileImg").value("https://drive.google.com/file/d/1R9EIOoEWWgPUhY6e-t4VFuqMgknl7rm8/view?usp=sharing"))
            .andExpect(jsonPath("$.name").value("nintendo"))
            .andExpect(jsonPath("$.category").value("GAMES"))
            .andExpect(jsonPath("$.images.size()").value(2))
            .andExpect(jsonPath("$.images[0]").value("png"))
            .andExpect(jsonPath("$.description").value("닌텐도 새 제품"))
            .andExpect(jsonPath("$.startPrice").value("5000"))
            .andExpect(jsonPath("$.currentPrice").value("10000"))
            .andExpect(jsonPath("$.status").value("IN_PROGRESS"))
            .andExpect(jsonPath("$.startDate").value("2024-01-02T00:00:00"))
            .andExpect(jsonPath("$.endDate").value("2024-01-04T00:00:00"))
            .andExpect(jsonPath("$.isMine").value("true"))
    }
}