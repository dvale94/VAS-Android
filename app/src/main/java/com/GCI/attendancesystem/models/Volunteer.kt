package com.GCI.attendancesystem.models

import java.io.Serializable

data class Volunteer(
    val caravailable: Boolean?,
    val currentstatus: String?,
    val email: String?,
    val firstname: String?,
    val id: String?,
    val lastname: String?,
    val major: String?,
    val mdcpsid: Int?,
    val pantherNo: String?,
    val position: String?,
    val userName: String?
) : Serializable