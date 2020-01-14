package com.GCI.attendancesystem.models

data class Schedule(
    val applicationUser: ApplicationUser?,
    val applicationUserId: String?,
    val classize: String?,
    val comments: String?,
    val createdby: String?,
    val dayofweekId: Int?,
    val endtime: String?,
    val school: School?,
    val schoolId: String?,
    val starttime: String?
)

data class NewSchedule(
    val classize: String,
    val comments: String,
    val dayoftheweek: Int,
    val endtime: String,
    val schoolid: String,
    val startime: String,
    val userid: String
)

data class UpdateSchedule(
    val classize: String,
    val comments: String,
    val createdby: String,
    val dayoftheweek: Int,
    val endtime: String,
    val schoolid: String,
    val startime: String,
    val userid: String
)

data class DeleteSchedule(
    val dayid: Int,
    val schoolid: String,
    val userid: String
)