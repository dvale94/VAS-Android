package com.GCI.attendancesystem.home.dashboard

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Chronometer
import android.widget.TextView
import android.widget.Toast
import android.widget.Toast.makeText
import androidx.recyclerview.widget.RecyclerView
import com.GCI.attendancesystem.R
import com.GCI.attendancesystem.models.Schedule
import com.GCI.attendancesystem.models.School
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import android.os.SystemClock
import br.com.simplepass.loadingbutton.customViews.CircularProgressButton
import com.GCI.attendancesystem.models.Attendance

class DashboardAdapter(private val fragment: DashboardFragment, private val rows: List<IRow>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    interface IRow
    class CheckOutRow(val startTime: String, val location: String) :
        IRow
    class SchoolRow(val school: School?) :
        IRow
    class ScheduleRow(val schedule: Schedule?) :
        IRow

    class CheckOutViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val startTime: TextView = itemView.findViewById(R.id.tv_time_started)
        val location: TextView = itemView.findViewById(R.id.tv_location)
        val timer: Chronometer = itemView.findViewById(R.id.tv_timer)
        val checkOutButton: CircularProgressButton = itemView.findViewById(R.id.checkout_button)
    }

    class SchoolInfoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val schoolName: TextView = itemView.findViewById(R.id.tv_name)
        val schoolAddr: TextView = itemView.findViewById(R.id.tv_school_address)
        val personnelName: TextView = itemView.findViewById(R.id.tv_personnel_name)
        val personnelEmail: TextView = itemView.findViewById(R.id.tv_personnel_email)
    }

    class ScheduleInfoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val meetingDay: TextView = itemView.findViewById(R.id.tv_meeting_day)
        val startTime: TextView = itemView.findViewById(R.id.tv_start_time)
        val endTime: TextView = itemView.findViewById(R.id.tv_end_time)
    }

    override fun getItemCount() = rows.count()

    override fun getItemViewType(position: Int): Int =
        when (rows[position]) {
            is CheckOutRow -> TYPE_CHECKOUT
            is SchoolRow -> TYPE_SCHOOL_INFO
            is ScheduleRow -> TYPE_OUTREACH_INFO
            else -> throw IllegalArgumentException()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = when (viewType) {
        TYPE_CHECKOUT -> CheckOutViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.checkout_item, parent, false)
        )
        TYPE_SCHOOL_INFO -> SchoolInfoViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.school_info_item, parent, false)
        )
        TYPE_OUTREACH_INFO -> ScheduleInfoViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.outreach_info_item, parent, false)
        )
        else -> throw IllegalArgumentException()
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) =
        when (holder.itemViewType) {
            TYPE_CHECKOUT -> onBindCheckOut(holder, rows[position] as CheckOutRow)
            TYPE_SCHOOL_INFO -> onBindSchool(holder, rows[position] as SchoolRow)
            TYPE_OUTREACH_INFO -> onBindSchedule(holder, rows[position] as ScheduleRow)
            else -> throw IllegalArgumentException()
        }

    private fun onBindCheckOut(holder: RecyclerView.ViewHolder, row: CheckOutRow) {
        val checkOutRow = holder as CheckOutViewHolder
        checkOutRow.startTime.text = row.startTime
        checkOutRow.location.text = row.location
        checkOutRow.timer.base = SystemClock.elapsedRealtime()
        checkOutRow.timer.start()
        checkOutRow.timer.format = "%m"
        checkOutRow.checkOutButton.setOnClickListener {
            checkOutRow.checkOutButton.startAnimation()
            if (!fragment.currentAttendanceId.isNullOrEmpty()) {
                fragment.apiService.finalizeAttendanceEntry(
                    "Bearer ${fragment.sp.getString("gciUser", "")}",
                    fragment.currentAttendanceId!!)
                    .enqueue(object: Callback<Attendance> {
                        override fun onFailure(call: Call<Attendance>, t: Throwable) {
                            makeText(fragment.activity, "Unable to check out",
                                Toast.LENGTH_LONG).show()
                        }

                        override fun onResponse(call: Call<Attendance>, response: Response<Attendance>) {
                            checkOutRow.timer.stop()
                            println(call.request())
                            println(response.body())
                            fragment.removeCheckout()
                        }
                    })
            }
        }
    }

    private fun onBindSchool(holder: RecyclerView.ViewHolder, row: SchoolRow) {
        val schoolRow = holder as SchoolInfoViewHolder
        schoolRow.schoolName.text = row.school?.name
        schoolRow.schoolAddr.text = row.school?.address
        if (row.school?.schoolPersonnel!!.isNotEmpty()){
            schoolRow.personnelName.text = "Personnel Name: $row.school.schoolPersonnel[0].firstname"
            schoolRow.personnelEmail.text = "Personnel Email: $row.school.schoolPersonnel[0].email"
        } else {
            schoolRow.personnelName.text = "Personnel Name: TBA"
            schoolRow.personnelEmail.text = "Personnel Email: TBA"
        }
    }

    private fun onBindSchedule(holder: RecyclerView.ViewHolder, row: ScheduleRow) {
        val scheduleRow = holder as ScheduleInfoViewHolder
        var dayOfWeekText: String? = null
        when (row.schedule?.dayofweekId) {
            0 -> dayOfWeekText = "Monday"
            1 -> dayOfWeekText = "Tuesday"
            2 -> dayOfWeekText = "Wednesday"
            3 -> dayOfWeekText = "Thursday"
            4 -> dayOfWeekText = "Friday"
        }
        scheduleRow.meetingDay.text = dayOfWeekText
        scheduleRow.startTime.text = formatDate(row.schedule?.starttime)
        scheduleRow.endTime.text = formatDate(row.schedule?.endtime)
    }

    private fun formatDate(date: String?): String {
        val localDateTime = LocalDateTime.parse(date)
        val formatter = DateTimeFormatter.ofPattern("HH:mm a")
        return formatter.format(localDateTime)
    }

    companion object {
        private const val TYPE_CHECKOUT = 0
        private const val TYPE_SCHOOL_INFO = 1
        private const val TYPE_OUTREACH_INFO = 2
    }
}