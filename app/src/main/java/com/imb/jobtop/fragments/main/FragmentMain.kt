package com.imb.jobtop.fragments.main

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import com.imb.jobtop.R
import com.imb.jobtop.fragments.FragmentMainVacancy
import com.imb.jobtop.fragments.resume.FragmentProfile
import com.imb.jobtop.fragments.JobListFragment
import com.imb.jobtop.fragments.base.BaseFragment
import com.imb.jobtop.model.Job
import com.imb.jobtop.model.Jobs
import com.imb.jobtop.model.User
import com.imb.jobtop.utils.extensions.putData
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
                    val data = Firebase.firestore.collection("users").document(Firebase.auth.uid!!)
                    data.get().addOnSuccessListener { d ->
                        var list: List<String>?
                        val u = d.toObject<User>()
                        list = u?.favorites
                        for (i in list!!) {
                            val splitted = i.split(",")
                        }

                        val b = Bundle()

//                        b.putData("data_list", Jobs(list))
                        chooseFragment(1, b)
                    }
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