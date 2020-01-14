package com.GCI.attendancesystem.admin.schools

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.GCI.attendancesystem.R
import com.GCI.attendancesystem.admin.schools.SchoolsAdapter.SchoolViewHolder
import com.GCI.attendancesystem.interfaces.OnItemClickListener
import com.GCI.attendancesystem.models.School

class SchoolsAdapter (private val schools: List<School>, val itemClickListener: OnItemClickListener):
    RecyclerView.Adapter<SchoolViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SchoolViewHolder {
        return SchoolViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.admin_item, parent, false))
    }

    override fun getItemCount(): Int {
        return schools.count()
    }

    override fun onBindViewHolder(holder: SchoolViewHolder, position: Int) {
        holder.bind(schools[position], itemClickListener)
    }

    class SchoolViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        private val schoolName: TextView = itemView.findViewById(R.id.textView)

        fun bind(school: School, clickListener: OnItemClickListener) {
            schoolName.text = school.name
            itemView.setOnClickListener {
                clickListener.onItemClicked(school)
            }
        }
    }
}