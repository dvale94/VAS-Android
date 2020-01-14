package com.GCI.attendancesystem.profile

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.GCI.attendancesystem.R
import com.GCI.attendancesystem.login.LoginActivity
import com.GCI.attendancesystem.models.User
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.fragment_profile.*

class ProfileFragment : Fragment() {

    lateinit var sp: SharedPreferences
    private var currentUser: User? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_profile, container, false)
        setHasOptionsMenu(true)
        return root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.profile_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId

        if (id == R.id.menu_logout) {
            activity?.let {
                sp.edit().putBoolean("logged", false).apply()
                val intent = Intent(context, LoginActivity::class.java)
                it.startActivity(intent)
            }
            return true
        }

        return super.onOptionsItemSelected(item)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        activity.let {
            sp = it!!.getSharedPreferences("login", AppCompatActivity.MODE_PRIVATE)
            val gson = Gson()
            val json = sp.getString("currentUser", "")
            val obj = gson.fromJson(json, User::class.java)

            obj.let {
                currentUser = obj
                handleCurrentUserInfo()
            }
        }
    }

    private fun handleCurrentUserInfo() {
        et_profile_username.apply {
            setText(currentUser?.userName)
        }
        et_profile_email.apply {
            setText(currentUser?.email)
        }
        et_profile_pantherNo.apply {
            setText(currentUser?.pantherNo)
        }
    }
}