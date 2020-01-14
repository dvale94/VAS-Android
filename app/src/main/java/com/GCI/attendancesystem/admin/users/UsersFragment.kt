package com.GCI.attendancesystem.admin.users

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.GCI.attendancesystem.R
import com.GCI.attendancesystem.interfaces.OnItemClickListener
import com.GCI.attendancesystem.models.Volunteer
import com.GCI.attendancesystem.networking.GCIApiService
import kotlinx.android.synthetic.main.fragment_users.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UsersFragment : Fragment(), OnItemClickListener {

    private val volunteers = mutableListOf<Volunteer>()
    private val apiService = GCIApiService.create()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragment_users, container, false)

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        getAllVolunteers()
        configureRecyclerView()
    }

    private fun configureRecyclerView() {
        val dividerItemDecoration = DividerItemDecoration(context, DividerItemDecoration.VERTICAL)
        context?.let { context ->
            ContextCompat.getDrawable(context, R.drawable.divider)?.let {
                dividerItemDecoration.setDrawable(
                    it
                )
            }
        }

        rv_members.apply {
            layoutManager = LinearLayoutManager(activity)
            addItemDecoration(dividerItemDecoration)
            adapter = UsersAdapter(volunteers, this@UsersFragment)
        }
    }

    private fun getAllVolunteers() {
        apiService.getAllUsers().enqueue(object: Callback<List<Volunteer>> {
            override fun onFailure(call: Call<List<Volunteer>>, t: Throwable) {
                println(t.message)
            }

            override fun onResponse(
                call: Call<List<Volunteer>>,
                response: Response<List<Volunteer>>
            ) {
                response.body().let {
                    for (user in response.body()!!) {
                        volunteers.add(user)
                    }
                    rv_members.adapter?.notifyDataSetChanged()
                }
            }

        })
    }

    override fun onItemClicked(model: Any) {
        val volunteer = model as Volunteer
        activity?.let {
            val bundle = Bundle()
            bundle.putSerializable("VOLUNTEER", volunteer)
            val intent = Intent(context, UserDetailActivity::class.java)
            intent.putExtras(bundle)
            it.startActivity(intent)
        }
    }

}