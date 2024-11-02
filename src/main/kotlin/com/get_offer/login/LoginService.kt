package com.get_offer.login

import com.get_offer.common.jwt.TokenService
import com.get_offer.user.service.UserService
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class LoginService(
    private val userService: UserService,
    private val tokenService: TokenService,
) {
    @Transactional
    fun sign(authMember: GoogleUser): String {

        // 로그인 or 회원가입
        val userId = userService.getSignedUser(authMember.email) ?: userService.createUser(
            authMember.name,
            authMember.picture,
            authMember.email
        )

        // token
        return tokenService.issue("$userId")
    }
}