package com.get_offer.user.controller

data class TokenRequest(
    val redirectUri: String,
    val authorizationCode: String,
)