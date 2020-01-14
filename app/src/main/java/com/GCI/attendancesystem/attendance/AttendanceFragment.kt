package com.GCI.attendancesystem.attendance

import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.GCI.attendancesystem.R
import com.GCI.attendancesystem.models.Attendance
import com.GCI.attendancesystem.models.UserAttendance
import com.GCI.attendancesystem.networking.GCIApiService
import kotlinx.android.synthetic.main.fragment_attendance.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AttendanceFragment : Fragment() {

    private val attendanceLog = mutableListOf<Attendance>()
    private val apiService = GCIApiService.create()
    private lateinit var sp: SharedPreferences

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_attendance, container, false)
        getUserAttendance()
        return root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        rv_attendance_log.apply {
            layoutManager = LinearLayoutManager(activity)
            adapter = AttendanceAdapter(attendanceLog)
        }
    }

    private fun getUserAttendance() {
        if (activity != null) {
            sp = activity!!.getSharedPreferences("login", AppCompatActivity.MODE_PRIVATE)
            apiService.getAllAttendanceLogs("Bearer ${sp.getString("gciUser", "")}").enqueue(object: Callback<UserAttendance> {
                override fun onFailure(call: Call<UserAttendance>, t: Throwable) {
                    Log.e("Attendance", t.message)
                }

                override fun onResponse(
                    call: Call<UserAttendance>,
                    response: Response<UserAttendance>
                ) {
                    println(response.body())
                    if (response.body()?.attendances?.isNotEmpty()!!) {
                        for (attendance in response.body()!!.attendances) {
                            attendanceLog.add(attendance)
                        }
                    }
                    rv_attendance_log.adapter?.notifyDataSetChanged()
                }
            })
        }
    }
}