package com.GCI.attendancesystem.admin.users

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.GCI.attendancesystem.R
import com.GCI.attendancesystem.admin.item.ListItem
import com.GCI.attendancesystem.models.NetworkResponse
import com.GCI.attendancesystem.models.User
import com.GCI.attendancesystem.models.Volunteer
import com.GCI.attendancesystem.networking.GCIApiService
import com.GCI.attendancesystem.networking.UserLogin
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.thejuki.kformmaster.helper.*
import kotlinx.android.synthetic.main.activity_user_detail.*
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Response

class UserDetailActivity : AppCompatActivity() {
    private lateinit var formBuilder: FormBuildHelper
    private var volunteer: Volunteer? = null
    private val apiService = GCIApiService.create()

    private enum class Tag {
        userName,
        email,
        pantherNo,
        role
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_detail)
        handleIntent()
        setupForm(recyclerView_users)
        configureActionBar()

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        if (volunteer != null) menuInflater.inflate(R.menu.user_changes_menu, menu)
        else menuInflater.inflate(R.menu.add_user_menu, menu)
        return true
    }

    override fun onPrepareOptionsMenu(menu: Menu?): Boolean {
        volunteer.let {
            val saveItem = menu?.findItem(R.id.menu_user_save)
            saveItem?.isEnabled = formBuilder.isValidForm
            val createItem = menu?.findItem(R.id.menu_add_user)
            createItem?.isEnabled = formBuilder.isValidForm
        }
        return super.onPrepareOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId

        if (volunteer != null) {
            when (id) {
                R.id.menu_user_save -> {
                    Toast.makeText(this, "Coming Soon", Toast.LENGTH_LONG).show()
                    return true
                }
                R.id.menu_delete_user -> {
                    Toast.makeText(this, "Coming Soon", Toast.LENGTH_LONG).show()
                    return true
                }
            }
        } else {
            if (id == R.id.menu_add_user) {
                createUser()
                return true
            }
        }

        return super.onOptionsItemSelected(item)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    private fun handleIntent() {
        val intent = intent
        val extras = intent.extras
        volunteer = extras?.getSerializable("VOLUNTEER") as Volunteer?
    }

    private fun configureActionBar() {
        val actionbar = supportActionBar
        actionbar?.title = "Details"
        actionbar?.setDisplayHomeAsUpEnabled(true)
        actionbar?.setDisplayShowHomeEnabled(true)
    }

    private fun createUser() {
        val newUser = User(formBuilder.elements[2].value as String?,
            "FiuPassword", formBuilder.elements[3].value as String?,
            formBuilder.elements[6].value as String?, formBuilder.elements[1].value as String?)

        apiService.registerUser(newUser).enqueue(object: retrofit2.Callback<NetworkResponse> {
            override fun onFailure(call: Call<NetworkResponse>, t: Throwable) {
                println(t.message)
            }

            override fun onResponse(
                call: Call<NetworkResponse>,
                response: Response<NetworkResponse>
            ) {
                println(call.request())
                println(response.raw())
                println(response.body())
            }

        })
    }

    private fun setupForm(recyclerView: RecyclerView) {
        formBuilder = form(this, recyclerView, cacheForm = true) {
            header {
                title = "USER INFORMATION"
            }
            text(Tag.userName.ordinal) {
                title = "Username"
                value = volunteer?.userName
                hint = "Username"
                required = true
            }
            email(Tag.email.ordinal) {
                title = "Email"
                value = volunteer?.email
                hint = "Email"
                required = true
            }
            number(Tag.pantherNo.ordinal) {
                title = "Panther Number"
                value = volunteer?.pantherNo
                hint = "1234567"
                maxLength = 7
                required = true
            }
            text {
                title = "First Name"
                value = volunteer?.firstname
                hint = "Jo"
            }
            text {
                title = "Last Name"
                value = volunteer?.lastname
                hint = "Do"
            }
            text(Tag.role.ordinal) {
                title = "Position"
                value = volunteer?.position
                hint = "Volunteer"
            }
            text {
                title = "Major"
                value = volunteer?.major
                hint = "Computer Science"
            }
            segmented<ListItem> {
                title = "Car Available?"
                fillSpace = true
                volunteer?.let { volunteer1 ->
                    volunteer1.caravailable.let {
                        value = if (it!!) ListItem(0, "Yes") else ListItem(1, "No")
                    }
                }
                options = listOf(
                    ListItem(0, "Yes"),
                    ListItem(1, "No")
                )
            }
            text {
                title = "Current Status"
                value = volunteer?.currentstatus
                hint = "Matched"
            }
            number {
                title = "MDCPS ID"
                volunteer?.mdcpsid.let {
                    value = it.toString()
                }
                layoutPaddingBottom = 80
                hint = "1234567"
            }

        }
    }
}
