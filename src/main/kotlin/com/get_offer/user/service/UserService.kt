package com.get_offer.user.service

import com.get_offer.common.exception.ApiException
import com.get_offer.common.exception.ExceptionCode
import com.get_offer.user.domain.User
import com.get_offer.user.domain.UserRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = false)
class UserService(
    private val userRepository: UserRepository,
) {
    fun getUserInfo(id: Long): UserInfoDto {
        return UserInfoDto.of(userRepository
            .findById(id).orElseThrow { ApiException(ExceptionCode.NOTFOUND, "$id 의 사용자는 존재하지 않습니다.") })
    }

    @Transactional
    fun createUser(nickname: String, picture: String?, email: String): Long {
        val user = User.of(
            nickname,
            picture,
            email
        )

        userRepository.save(user)
        return user.id
    }

    fun getSignedUser(email: String): Long? {
        return userRepository.findByEmail(email)?.id
    }
}