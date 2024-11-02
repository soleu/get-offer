package com.get_offer.common.jwt

import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.Keys
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.*
import org.springframework.stereotype.Service

@Service
class TokenService(
    private val jwtProperties: JwtProperties,
) {

    private val secretKey = Keys.hmacShaKeyFor(
        jwtProperties.key.toByteArray()
    )


    fun issue(
        subject: String,
        expirationDate: LocalDateTime = LocalDateTime.now().plusSeconds(jwtProperties.accessTokenExpiration)
    ): String = Jwts.builder()
        .claims()
        .subject(subject)
        .issuedAt(Date(System.currentTimeMillis()))
        .expiration(Date.from(expirationDate.atZone(ZoneId.systemDefault()).toInstant()))
        .and()
        .signWith(secretKey)
        .compact()

    fun extractSubject(
        token: String
    ): String? = getAllClaims(token)
        .subject

    fun isExpired(token: String): Boolean =
        getAllClaims(token)
            .expiration
            .before(Date(System.currentTimeMillis()))

    private fun getAllClaims(
        token: String
    ): Claims = Jwts.parser()
        .verifyWith(secretKey)
        .build()
        .parseSignedClaims(token)
        .payload
}