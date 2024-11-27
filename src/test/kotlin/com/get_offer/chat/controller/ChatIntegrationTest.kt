package com.get_offer.chat.controller

import com.get_offer.login.jwt.TokenService
import java.time.LocalDateTime
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.jdbc.Sql
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultHandlers
import org.springframework.test.web.servlet.result.MockMvcResultMatchers

@SpringBootTest
@Sql("classpath:test_data.sql")
@AutoConfigureMockMvc
class ChatIntegrationTest(
    @Autowired val mockMvc: MockMvc,
    @Autowired val tokenService: TokenService,
) {
    lateinit var tokenUser1: String

    @BeforeEach
    fun setUp() {
        tokenUser1 = tokenService.issue("1", LocalDateTime.now().plusDays(1))
    }

    @Test
    fun `createChatRoom`() {
        mockMvc.perform(
            MockMvcRequestBuilders.post("/chat/4/start")
                .header("Authorization", tokenUser1)
        )
            .andDo(MockMvcResultHandlers.print())
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.jsonPath("$.roomId").exists())
            .andExpect(MockMvcResultMatchers.jsonPath("$.roomId").value("1"))
    }

    @Test
    fun `getChatHistory`() {
        mockMvc.perform(
            MockMvcRequestBuilders.get("/chat/1/history")
                .header("Authorization", tokenUser1)
        )
            .andDo(MockMvcResultHandlers.print())
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.jsonPath("$[0].chatRoomId").value("1"))
            .andExpect(MockMvcResultMatchers.jsonPath("$[0].senderId").value("1"))
            .andExpect(MockMvcResultMatchers.jsonPath("$[0].content").value("안녕하세요"))
    }
}
