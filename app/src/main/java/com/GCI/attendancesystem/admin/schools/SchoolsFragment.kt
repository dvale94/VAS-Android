package com.GCI.attendancesystem.admin.schools

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.GCI.attendancesystem.R
import com.GCI.attendancesystem.interfaces.OnItemClickListener
import com.GCI.attendancesystem.models.School
import com.GCI.attendancesystem.networking.GCIApiService
import kotlinx.android.synthetic.main.fragment_schools.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SchoolsFragment : Fragment(), OnItemClickListener {

    private val schools = mutableListOf<School>()
    private val apiService = GCIApiService.create()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragment_schools, container, false)

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        getAllSchools()
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

        rv_schools.apply {
            layoutManager = LinearLayoutManager(activity)
            addItemDecoration(dividerItemDecoration)
            adapter = SchoolsAdapter(schools, this@SchoolsFragment)
        }
    }

    private fun getAllSchools() {
        apiService.getAllSchools().enqueue(object: Callback<List<School>> {
            override fun onFailure(call: Call<List<School>>, t: Throwable) {
                println(t.message)
            }

            override fun onResponse(call: Call<List<School>>, response: Response<List<School>>) {
                response.body().let {
                     for (school in response.body()!!) {
                         schools.add(school)
                     }
                     rv_schools.adapter?.notifyDataSetChanged()
                 }
            }

        })
    }

    override fun onItemClicked(model: Any) {
        val school = model as School
        activity?.let {
            val bundle = Bundle()
            bundle.putSerializable("SCHOOL", school)
            val intent = Intent(context, SchoolDetailActivity::class.java)
            intent.putExtras(bundle)
            it.startActivity(intent)
        }
    }

}