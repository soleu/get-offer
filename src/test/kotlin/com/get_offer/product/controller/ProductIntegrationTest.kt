package com.get_offer.product.controller

import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.jdbc.Sql
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultHandlers
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@SpringBootTest
@AutoConfigureMockMvc
@SpringJUnitConfig
@Sql("/sql/product-schema.sql", "/sql/user-schema.sql")
class ProductIntegrationTest(
    @Autowired val mockMvc: MockMvc,
) {

    @Test
    fun activeProductListControllerTest() {
        mockMvc.perform(
            get("/products/activeList").param("userId", "1")
        ).andDo(MockMvcResultHandlers.print()).andExpect(status().isOk).andExpect(jsonPath("$.size()").value(1))
            .andExpect(jsonPath("$[0].writerId").value("1")).andExpect(jsonPath("$[0].name").value("nintendo"))
            .andExpect(jsonPath("$[0].category").value("GAMES")).andExpect(jsonPath("$[0].thumbNail").value("png"))
            .andExpect(jsonPath("$[0].images.size()").value(2)).andExpect(jsonPath("$[0].images[0]").value("png"))
            .andExpect(jsonPath("$[0].description").value("닌텐도 새 제품"))
            .andExpect(jsonPath("$[0].currentPrice").value("10000"))
            .andExpect(jsonPath("$[0].status").value("IN_PROGRESS"))
            .andExpect(jsonPath("$[0].startDate").value("2024-01-02T00:00:00"))
            .andExpect(jsonPath("$[0].endDate").value("2024-01-04T00:00:00"))
            .andExpect(jsonPath("$[0].isMine").value("true"))
    }
}

