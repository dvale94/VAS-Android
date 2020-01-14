package com.GCI.attendancesystem.home

import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.GCI.attendancesystem.R
import com.GCI.attendancesystem.models.Schedule
import com.GCI.attendancesystem.models.Volunteer
import com.GCI.attendancesystem.networking.GCIApiService
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeActivity : AppCompatActivity() {
    private lateinit var sp: SharedPreferences
    private val apiService = GCIApiService.create()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        sp = getSharedPreferences("login",MODE_PRIVATE)

        val gson = Gson()
        val json = sp.getString("currentUser", "")
        val obj = gson.fromJson(json, Volunteer::class.java)

        Toast.makeText(this@HomeActivity,
            obj.email,
            Toast.LENGTH_LONG).show()

        getUserSchedule()

        val navView: BottomNavigationView = findViewById(R.id.nav_view)

        val navController = findNavController(R.id.nav_host_fragment)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_dashboard, R.id.navigation_attendance, R.id.navigation_admin,
                R.id.navigation_profile
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
    }

    private fun getUserSchedule() {
        val gson = Gson()
        val json = sp.getString("currentUser", "")
        val obj = gson.fromJson(json, Volunteer::class.java)
        val editor = sp.edit()

        apiService.getAllSchedules().enqueue(object: Callback<List<Schedule>> {
            override fun onFailure(call: Call<List<Schedule>>, t: Throwable) {
                Log.e("Attendance", t.message)
            }

            override fun onResponse(
                call: Call<List<Schedule>>,
                response: Response<List<Schedule>>
            ) {

                if (response.body() != null){
                    for (schedule in response.body()!!) {
                        if (schedule.applicationUser!!.email == obj.email) {
                            val gsonSchedule = Gson()
                            val jsonSchedule = gsonSchedule.toJson(schedule)
                            editor.putString("currentUserSchedule", jsonSchedule).apply()
                        }
                    }
                }
            }

        })
    }
}
