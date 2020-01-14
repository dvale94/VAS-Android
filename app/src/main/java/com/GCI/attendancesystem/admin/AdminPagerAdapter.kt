package com.GCI.attendancesystem.admin

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.GCI.attendancesystem.admin.schools.SchoolsFragment
import com.GCI.attendancesystem.admin.users.UsersFragment

class AdminPagerAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    override fun getItem(position: Int): Fragment {
        return when (position) {
            0 -> {
                SchoolsFragment()
            }
            1 -> UsersFragment()
            else -> {
                return UsersFragment()
            }
        }
    }

    override fun getCount(): Int {
        return 2
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return when (position) {
            0 -> "Schools"
            1 -> "Members"
            else -> {
                return "N/A"
            }
        }
    }
}