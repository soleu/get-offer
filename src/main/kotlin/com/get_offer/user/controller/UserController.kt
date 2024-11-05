package com.get_offer.user.controller

import ApiResponse
import com.get_offer.login.AuthenticatedUser
import com.get_offer.user.service.UserInfoDto
import com.get_offer.user.service.UserService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController


@RestController
class UserController(
    private val userService: UserService,
) {
    @GetMapping("/users")
    fun getUserInfo(@AuthenticatedUser userId: Long): ApiResponse<UserInfoDto> {
        return ApiResponse.success(userService.getUserInfo(userId))
    }
}