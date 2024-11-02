package com.get_offer.common.jwt

import com.get_offer.user.domain.User
import com.get_offer.user.repository.UserRepository
import jakarta.servlet.FilterChain
import jakarta.servlet.ServletRequest
import jakarta.servlet.ServletResponse
import jakarta.servlet.http.HttpServletRequest
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.filter.GenericFilterBean

class JwtAuthenticationFilter(
    private val tokenService: TokenService,
    private val userRepository: UserRepository,
) : GenericFilterBean() {
    override fun doFilter(request: ServletRequest?, response: ServletResponse?, chain: FilterChain?) {
        val httpRequest = request as? HttpServletRequest ?: return chain?.doFilter(request, response)!!
        val token = httpRequest.getHeader("Authorization")

        try {
            token?.takeIf { tokenService.verifyToken(it) }?.let {
                tokenService.extractSubject(it)?.let { email ->
                    userRepository.findByEmail(email)?.let { user ->
                        val auth = getAuthentication(user)
                        SecurityContextHolder.getContext().authentication = auth
                    }
                }
            }
        } catch (e: Exception) {
            // 예외를 로깅하여 디버깅
            logger.warn("JWT 필터 처리 중 오류 발생: ${e.message}")
        }
        chain?.doFilter(request, response)
    }

    private fun getAuthentication(user: User): Authentication {
        return UsernamePasswordAuthenticationToken(user, "", listOf(SimpleGrantedAuthority("ROLE_USER")))
    }
}
