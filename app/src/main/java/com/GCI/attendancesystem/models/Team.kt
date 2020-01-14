package com.GCI.attendancesystem.models

data class Team(
    val description: String?,
    val id: String?,
    val teamnumber: String?,
    val volunteer: List<Volunteer>?
)