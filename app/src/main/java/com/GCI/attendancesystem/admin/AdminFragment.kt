package com.GCI.attendancesystem.admin

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager
import androidx.viewpager.widget.ViewPager.OnPageChangeListener
import com.GCI.attendancesystem.R
import com.google.android.material.tabs.TabLayout
import kotlinx.android.synthetic.main.fragment_admin.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat.invalidateOptionsMenu
import com.GCI.attendancesystem.admin.schools.SchoolDetailActivity
import com.GCI.attendancesystem.admin.users.UserDetailActivity


class AdminFragment : Fragment(){

    private lateinit var viewPager: ViewPager
    private lateinit var tabLayout: TabLayout

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val root = inflater.inflate(R.layout.fragment_admin, container, false)

        viewPager = root.findViewById(R.id.viewpager_admin)
        tabLayout = root.findViewById(R.id.tabs_admin)
        setHasOptionsMenu(true)

        configureTabLayout()

        return root

    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        if (viewPager.currentItem == 0) {
            inflater.inflate(R.menu.add_school_menu, menu)
        } else {
            inflater.inflate(R.menu.add_user_menu, menu)
        }
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId

        if (id == R.id.menu_add_school) {
            activity?.let {
                val intent = Intent(context, SchoolDetailActivity::class.java)
                it.startActivity(intent)
            }
            return true
        }
        if (id == R.id.menu_add_user) {
            activity?.let {
                val intent = Intent(context, UserDetailActivity::class.java)
                it.startActivity(intent)
            }
            return true
        }

        return super.onOptionsItemSelected(item)
    }

    private fun configureTabLayout() {
        tabLayout.addTab(tabLayout.newTab().setText("Schools"))
        tabLayout.addTab(tabLayout.newTab().setText("Members"))

        val fragmentAdapter = AdminPagerAdapter(childFragmentManager)
        viewPager.adapter = fragmentAdapter
        handlePageChange()

        tabLayout.setupWithViewPager(viewPager)
    }

    private fun handlePageChange() {
        viewPager.addOnPageChangeListener(object: OnPageChangeListener {
            override fun onPageScrollStateChanged(state: Int) {
                activity.let {
                    activity?.invalidateOptionsMenu()
                }
            }

            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
                //
            }

            override fun onPageSelected(position: Int) {
                //
            }

        })
    }

}
