package com.GCI.attendancesystem.models

data class Attendance(
    val id: String?,
    val pantherid: String?,
    val date: String?,
    val signInTime: String?,
    val signOutTime: String?,
    val notes: String?
)

data class UserAttendance(
    val attendances: List<Attendance>,
    val email: String,
    val id: String,
    val pantherNo: String,
    val userName: String
)