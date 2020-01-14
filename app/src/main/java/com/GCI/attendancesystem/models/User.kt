package com.GCI.attendancesystem.models

data class User(
    val email: String?,
    val password: String? = "password",
    val pantherNo: String?,
    val role: String?,
    val userName: String?
)