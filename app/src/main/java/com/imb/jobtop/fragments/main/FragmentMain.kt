package com.imb.jobtop.fragments.main

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.imb.jobtop.R
import com.imb.jobtop.fragments.FragmentMainVacancy
import com.imb.jobtop.fragments.resume.FragmentProfile
import com.imb.jobtop.fragments.JobListFragment
import com.imb.jobtop.fragments.base.BaseFragment
import kotlinx.android.synthetic.main.fragment_main.*

class FragmentMain : BaseFragment(R.layout.fragment_main) {

    private val pages = arrayOf<Fragment>(
        FragmentMainVacancy(),
        JobListFragment(),
        FragmentProfile()
    )
    private var activePage = 0


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initBottomNavBar()
        closeKeyboard()
        chooseFragment(activePage)
    }

    private fun initBottomNavBar() {
        bottomNavigation.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.homePage -> {
                    chooseFragment(0)
                }
                R.id.favPage -> {
                    val b = Bundle()
                    b.putInt("type", 1)
                    chooseFragment(1, b)
                }
                R.id.userPage -> {
                    chooseFragment(2)
                }
            }
            true
        }
    }

    private fun chooseFragment(i: Int, bundle: Bundle? = null) {
        activePage = i
        bundle?.let {
            pages[i].arguments = bundle
        }
        childFragmentManager
            .beginTransaction()
            .replace(R.id.container, pages[i])
            .commit()
    }


}