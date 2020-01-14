package com.GCI.attendancesystem.networking

import com.GCI.attendancesystem.models.*
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*
import java.util.*

data class UserLogin(val userName: String,
                     val password: String)

interface GCIApiService {

    // USER LOGIN & REGISTRATION
    @Headers("Content-Type: application/json")
    @POST("ApplicationUser/Login")
    fun loginUser(@Body userLogin: UserLogin): Call<Token>

    @Headers("Content-Type: application/json")
    @POST("ApplicationUser/Register")
    fun registerUser(@Body newUser: User): Call<NetworkResponse>

    // ATTENDANCE
    @GET("Attendance")
    fun getAllAttendanceLogs(@Header("Authorization") withToken: String): Call<UserAttendance>

    @Headers("Content-Type: application/json")
    @POST("Attendance/signintime")
    fun createAttendanceEntry(@Header("Authorization") withToken: String,
                              @Body notes: String = "TBA"): Call<Attendance>

    @PUT("Attendance/signout/")
    fun finalizeAttendanceEntry(@Header("Authorization") withToken: String,
                                @Field("attendanceid") id: String): Call<Attendance>

    @DELETE("Attendance")
    fun deleteAttendanceEntry(@Header("Authorization") withToken: String,
                              @Field("attendanceid") id: String): Call<Boolean>

    // SCHEDULE
    @GET("Schedule")
    fun getAllSchedules(): Call<List<Schedule>>

    @Headers("Content-Type: application/json")
    @POST("Schedule/create")
    fun createSchedule(@Body schedule: NewSchedule): Call<Schedule>

    @Headers("Content-Type: application/json")
    @PUT("Schedule")
    fun updateSchedule(@Body schedule: UpdateSchedule): Call<Boolean>

    @Headers("Content-Type: application/json")
    @DELETE("Schedule")
    fun deleteSchedule(@Body schedule: DeleteSchedule): Call<Boolean>

    // SCHOOL
    @GET("School")
    fun getAllSchools(): Call<List<School>>

    @Headers("Content-Type: application/json")
    @POST("School")
    fun createSchool(): Call<List<School>>

    @GET("School")
    fun getSchool(@Field("id") id: String): Call<School>

    @GET("School")
    fun updateSchool(@Field("schoolid") id: String): Call<Boolean>

    @DELETE("School")
    fun deleteSchool(@Field("schoolid") id: String): Call<Boolean>

    @Headers("Content-Type: application/json")
    @POST("School/addpersonneltoschool")
    fun addPersonnelToSchool(@Body schoolid: String, @Body personnelid: String): Call<School>

    // TEAM
    @GET("Team")
    fun getAllTeams(): Call<List<Team>>

    @Headers("Content-Type: application/json")
    @POST("Team")
    fun createTeam(@Body team: Team): Call<Boolean>

    @Headers("Content-Type: application/json")
    @POST("Team")
    fun updateTeam(@Field("teamid") teamid: String, @Body team: Team): Call<Boolean>

    @DELETE("Team")
    fun deleteTeam(@Field("teamid") teamid: String): Call<Boolean>

    @Headers("Content-Type: application/json")
    @POST("School/addteamtoschool")
    fun addTeamToSchool(@Body schoolid: String, @Body teamid: String): Call<School>

    @Headers("Content-Type: application/json")
    @POST("School/removeteamtoschool")
    fun removeTeamToSchool(@Body schoolid: String, @Body teamid: String): Call<Boolean>

    @Headers("Content-Type: application/json")
    @POST("School/addusertoteam")
    fun addUserToTeam(@Body teamid: String, @Body userid: String): Call<Team>

    @Headers("Content-Type: application/json")
    @POST("School/removeusertoteam")
    fun removeUserToTeam(@Body teamid: String, @Body userid: String): Call<Team>

    // USER PROFILE
    @GET("UserProfile")
    fun authenticateUser(@Header("Authorization") withToken: String): Call<User>

    @GET("UserProfile/all")
    fun getAllUsers(): Call<List<Volunteer>>

    companion object Factory {
        fun create(): GCIApiService {
            val retrofit = Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl("http://commandsapiname.azurewebsites.net/api/")
                .build()

            return retrofit.create(GCIApiService::class.java)
        }
    }
}