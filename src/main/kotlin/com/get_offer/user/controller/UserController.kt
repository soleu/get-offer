package com.get_offer.user.controller

import ApiResponse
import com.get_offer.user.service.UserInfoDto
import com.get_offer.user.service.UserService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
class UserController(
    private val userService: UserService,
) {
    @GetMapping("/users")
    fun getUserInfo(@RequestParam userId: String): ApiResponse<UserInfoDto> {
        return ApiResponse.success(userService.getUserInfo(userId.toLong()))
    }

    @PostMapping("/login/oauth2/code/google")
    fun login(@RequestBody loginRequest: LoginRequest) {
        println("hi")
    }
}