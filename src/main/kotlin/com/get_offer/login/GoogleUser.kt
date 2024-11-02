package com.get_offer.login

data class GoogleUser(
    val email: String,
    val picture: String?,
    val name: String,
    val sub: String?
)