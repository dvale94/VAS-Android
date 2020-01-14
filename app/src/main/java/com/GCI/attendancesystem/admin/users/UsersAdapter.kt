package com.GCI.attendancesystem.admin.users

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.GCI.attendancesystem.R
import com.GCI.attendancesystem.admin.users.UsersAdapter.UserViewHolder
import com.GCI.attendancesystem.interfaces.OnItemClickListener
import com.GCI.attendancesystem.models.Volunteer

class UsersAdapter (private val schools: List<Volunteer>, val itemClickListener: OnItemClickListener):
    RecyclerView.Adapter<UserViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        return UserViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.admin_item, parent, false))
    }

    override fun getItemCount(): Int {
        return schools.count()
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        holder.bind(schools[position], itemClickListener)
    }

    class UserViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        private val userEmail: TextView = itemView.findViewById(R.id.textView)

        fun bind(user: Volunteer, clickListener: OnItemClickListener) {
            userEmail.text = user.email
            itemView.setOnClickListener {
                clickListener.onItemClicked(user)
            }
        }
    }
}