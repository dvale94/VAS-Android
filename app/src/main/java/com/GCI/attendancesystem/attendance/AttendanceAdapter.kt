package com.GCI.attendancesystem.attendance

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.GCI.attendancesystem.R
import com.GCI.attendancesystem.models.Attendance
import kotlinx.android.synthetic.main.attendance_item.view.*
import okhttp3.internal.format
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import com.GCI.attendancesystem.attendance.AttendanceAdapter.AttendanceViewHolder as AttendanceViewHolder1

class AttendanceAdapter(private val attendanceEntries: List<Attendance>):
    RecyclerView.Adapter<AttendanceViewHolder1>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AttendanceViewHolder {
        return AttendanceViewHolder(LayoutInflater.from(parent.context)
            .inflate(R.layout.attendance_item, parent, false))
    }

    override fun getItemCount(): Int {
        return attendanceEntries.count()
    }

    override fun onBindViewHolder(holder: AttendanceViewHolder, position: Int) {
        holder.bind(attendanceEntries[position])
    }

    class AttendanceViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val date = itemView.tv_date
        private val signInTime = itemView.tv_sign_in_time
        private val signOutTime = itemView.tv_sign_out_time
        private val notes = itemView.tv_notes

        fun bind(entry: Attendance) {
            if (!entry.date.isNullOrEmpty()) {
                date.text = formatDate(entry.date)
            }
            if (!entry.signInTime.isNullOrEmpty()) {
                signInTime.text = formatTime(entry.signInTime)
            }
            if (!entry.signOutTime.isNullOrEmpty()) {
                signOutTime.text = formatTime(entry.signOutTime)
            }
            notes.text = entry.notes
        }

        private fun formatDate(date: String?): String {
            val localDateTime = LocalDateTime.parse(date)
            val formatter = DateTimeFormatter.ofPattern("MM/dd/yy")
            return formatter.format(localDateTime)
        }

        private fun formatTime(time: String?): String {
            val localDateTime = LocalDateTime.parse(time)
            val formatter = DateTimeFormatter.ofPattern("hh:mm a")
            return formatter.format(localDateTime)
        }
    }
}
