package com.GCI.attendancesystem.admin.schools

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.GCI.attendancesystem.R
import com.GCI.attendancesystem.admin.item.ListItem
import com.GCI.attendancesystem.models.School
import com.GCI.attendancesystem.models.Team
import com.GCI.attendancesystem.networking.GCIApiService
import com.thejuki.kformmaster.helper.*
import kotlinx.android.synthetic.main.activity_school_detail.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*

class SchoolDetailActivity : AppCompatActivity() {
    private lateinit var formBuilder: FormBuildHelper
    private var school: School? = null
    private val teams = mutableListOf<ListItem>()
    private val apiService = GCIApiService.create()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_school_detail)
        configureActionBar()
        handleIntent()
        setupForm(recyclerView_schools)
        getAllTeams()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        if (school != null) menuInflater.inflate(R.menu.school_changes_menu, menu)
        else menuInflater.inflate(R.menu.add_school_menu, menu)
        return true
    }

    override fun onPrepareOptionsMenu(menu: Menu?): Boolean {
        school.let {
            val saveItem = menu?.findItem(R.id.menu_school_save)
            saveItem?.isEnabled = formBuilder.isValidForm
            val createItem = menu?.findItem(R.id.menu_add_school)
            createItem?.isEnabled = formBuilder.isValidForm
        }
        return super.onPrepareOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId

        if (school != null) {
            when (id) {
                R.id.menu_school_save -> {
                    Toast.makeText(this, "Save Changes", Toast.LENGTH_LONG).show()
                    return true
                }
                R.id.menu_delete_school -> {
                    Toast.makeText(this, "Delete School", Toast.LENGTH_LONG).show()
                    return true
                }
            }
        } else {
            if (id == R.id.menu_add_school) {
                Toast.makeText(this, "Add School", Toast.LENGTH_LONG).show()
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
        school = extras?.getSerializable("SCHOOL") as School?
    }

    private fun configureActionBar() {
        val actionbar = supportActionBar
        actionbar?.title = "Details"
        actionbar?.setDisplayHomeAsUpEnabled(true)
        actionbar?.setDisplayShowHomeEnabled(true)
    }

        private fun setupForm(recyclerView: RecyclerView) {
        formBuilder = form(this, recyclerView, cacheForm = true) {
            header {
                title = "SCHOOL INFORMATION"
            }
            number {
                title = "ID"
                hint = "School ID"
                value = school?.schoolid
                numbersOnly = true
                required = true
            }
            text {
                title = "Name"
                hint = "School Name"
                value = school?.name
            }
            textArea {
                title = "Address"
                hint = "School Address"
                value = school?.address
            }
            text {
                title = "Grades"
                hint = "Grades"
                value = school?.grade
            }
            phone {
                title = "Phone"
                hint = "School Phone"
                value = school?.phonenumber
            }

            header {
                title = "SCHOOL SCHEDULE"
            }
            segmented<ListItem> {
                title = "Meets?"
                fillSpace = true
                options = listOf(
                    ListItem(0, "M"),
                    ListItem(1, "T"),
                    ListItem(2, "W"),
                    ListItem(3, "TH"),
                    ListItem(4, "F")
                )
            }
            time {
                title = "Start Time"
                dateValue = Date()
                dateFormat = SimpleDateFormat("hh:mm a", Locale.US)
            }
            time {
                title = "End Time"
                dateValue = Date()
                dateFormat = SimpleDateFormat("hh:mm a", Locale.US)
            }
            number {
                title = "Class Size"
                hint = "12"
                numbersOnly = true
            }

            header {
                title = "Team(s)"
            }
            multiCheckBox<List<ListItem>> {
                title = ""
                options = teams
                layoutPaddingBottom = 80
            }
        }
    }

    private fun getAllTeams() {
        apiService.getAllTeams().enqueue(object : Callback<List<Team>> {
            override fun onFailure(call: Call<List<Team>>, t: Throwable) {
                println(t.message)
            }

            override fun onResponse(call: Call<List<Team>>, response: Response<List<Team>>) {
                response.body().let {
                    for (team in response.body()!!) {
                        teams.add(ListItem(null, team.description))
                    }
                }
            }

        })
    }
}
