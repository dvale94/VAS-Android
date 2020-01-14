package com.GCI.attendancesystem.home.dashboard

import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.GCI.attendancesystem.R
import com.GCI.attendancesystem.models.*
import com.GCI.attendancesystem.networking.GCIApiService
import com.google.gson.Gson
import kotlinx.android.synthetic.main.fragment_dashboard.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle

class DashboardFragment : Fragment() {

    lateinit var sp: SharedPreferences
    private val rows = mutableListOf<DashboardAdapter.IRow>()
    private lateinit var checkInButton: View
    var currentAttendanceId: String? = null
    val apiService = GCIApiService.create()
    val current = LocalDateTime.now()
    val formatter = DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT)
    val formatted = current.format(formatter)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        val root = inflater.inflate(R.layout.fragment_dashboard, container, false)
        if (activity != null) {
            sp = activity!!.getSharedPreferences("login", AppCompatActivity.MODE_PRIVATE)
            val gson = Gson()
            val json = sp.getString("currentUserSchedule", "")
            val obj = gson.fromJson(json, Schedule::class.java)

            if (obj != null) {
                if (obj.school != null) {
                    rows.add(
                        DashboardAdapter.SchoolRow(
                            obj.school
                        )
                    )
                }
                rows.add(DashboardAdapter.ScheduleRow(obj))
            }

            checkInButton = root.findViewById(R.id.floatingActionButton)

            if (obj != null) {
                checkInButton.setOnClickListener {
                    apiService.createAttendanceEntry("Bearer ${sp.getString("gciUser", "")}")
                        .enqueue(object: Callback<Attendance> {
                            override fun onResponse(
                                call: Call<Attendance>,
                                response: Response<Attendance>
                            ) {
                                println(call.request().body.toString())
                                println(response.body())
                                currentAttendanceId = response.body()?.id
                                obj.school?.name?.let { it1 ->
                                    DashboardAdapter.CheckOutRow(
                                        formatted,
                                        it1
                                    )
                                }?.let { it2 ->
                                    rows.add(0,
                                        it2
                                    )
                                }
                                rv_dashboard.adapter?.notifyItemInserted(0)
                                it.visibility = View.INVISIBLE
                            }

                            override fun onFailure(call: Call<Attendance>, t: Throwable) {
                                println(t)
                            }

                        })
                }
            }
        }

        return root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        rv_dashboard.apply {
            layoutManager = LinearLayoutManager(activity)
            adapter = DashboardAdapter(
                this@DashboardFragment,
                rows
            )
        }
    }

    fun removeCheckout() {
        currentAttendanceId = null
        rows.removeAt(0)
        rv_dashboard.adapter?.notifyItemRemoved(0)
        checkInButton.visibility = View.VISIBLE
    }
}