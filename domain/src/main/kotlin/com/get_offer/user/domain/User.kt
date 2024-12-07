package com.get_offer.user.domain

import com.get_offer.common.AuditingTimeEntity
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table

@Entity
@Table(name = "USERS")
class User(
    val nickname: String,

    val image: String = DEFAULT_IMG,

    val email: String = "",

    val phone: String = "",

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0L,
) : AuditingTimeEntity() {
    companion object {
        const val DEFAULT_IMG = "https://drive.google.com/file/d/1g5yH7rq4_6bMrahRUD3fMoFHIcVLY18y/view?usp=sharing"
        fun of(nickname: String, image: String?, email: String): User {
            return User(nickname = nickname, image = image ?: DEFAULT_IMG, email = email)
        }
    }
}