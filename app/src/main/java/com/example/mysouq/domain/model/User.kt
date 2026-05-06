package com.example.mysouq.domain.model

data class User(
    val id: String,
    val name: String,
    val email: String,
    val profilePictureUrl: String? = null,
    val city: String? = null,
    val phoneNumber: String? = null
)
