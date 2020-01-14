package com.GCI.attendancesystem.models

import java.io.Serializable

data class School(
    val address: String?,
    val grade: String?,
    val id: String?,
    val name: String?,
    val phonenumber: String?,
    val schoolPersonnel: List<SchoolPersonnel>?,
    val schoolid: String?,
    val teams: List<Team>?
) : Serializable

data class CreateSchool(
    val address: String,
    val grade: String,
    val id: String,
    val name: String,
    val outreachschedules: List<Outreachschedule>,
    val phonenumber: String,
    val schoolPersonnel: List<SchoolPersonnel>,
    val schoolid: String,
    val teams: List<Team>
)

data class Outreachschedule(
    val applicationUserId: String,
    val classize: String,
    val comments: String,
    val createdby: String,
    val dayofweek: Dayofweek,
    val dayofweekId: Int,
    val endtime: String,
    val schoolId: String,
    val starttime: String
)

data class Dayofweek(
    val id: Int
)

data class OutreachscheduleX(
    val applicationUserId: String,
    val classize: String,
    val comments: String,
    val createdby: String,
    val dayofweek: DayofweekX,
    val dayofweekId: Int,
    val endtime: String,
    val schoolId: String,
    val starttime: String
)

data class DayofweekX(
    val id: Int
)

data class AttendanceX(
    val date: String,
    val id: String,
    val notes: String,
    val pantherid: String,
    val signInTime: String,
    val signOutTime: String
)

data class OutreachscheduleXX(
    val applicationUserId: String,
    val classize: String,
    val comments: String,
    val createdby: String,
    val dayofweek: DayofweekXX,
    val dayofweekId: Int,
    val endtime: String,
    val schoolId: String,
    val starttime: String
)

data class DayofweekXX(
    val id: Int
)