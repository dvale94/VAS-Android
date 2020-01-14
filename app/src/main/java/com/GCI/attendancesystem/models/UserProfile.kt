package com.GCI.attendancesystem.models

data class UserProfile(
    val email: String,
    val pantherNo: String,
    val password: String,
    val role: String,
    val userName: String
)