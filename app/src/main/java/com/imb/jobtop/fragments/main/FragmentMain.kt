package com.imb.jobtop.fragments.main

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import com.imb.jobtop.R
import com.imb.jobtop.fragments.FragmentMainVacancy
import com.imb.jobtop.fragments.resume.FragmentProfile
import com.imb.jobtop.fragments.JobListFragment
import com.imb.jobtop.fragments.base.BaseFragment
import com.imb.jobtop.model.*
import com.imb.jobtop.utils.extensions.progressOff
import com.imb.jobtop.utils.extensions.progressOn
import com.imb.jobtop.utils.extensions.putData
import kotlinx.android.synthetic.main.fragment_main.*

class FragmentMain : BaseFragment(R.layout.fragment_main) {

    private val pages = arrayOf<Fragment>(
        FragmentMainVacancy(),
        JobListFragment(),
        FragmentProfile()
    )
    private var activePage = 0
    private lateinit var db: FirebaseFirestore

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        db = Firebase.firestore
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
                    progressOn()
                    val data = db.collection("users")
                        .document(Firebase.auth.currentUser!!.uid)
                    data.get().addOnSuccessListener { d ->
                        val list: List<String>?
                        val u = d.toObject<User>()
                        list = u?.favorites
                        val b = Bundle()
                        b.putInt("key", 1)
                        b.putData("data_list", Strings(list?.toMutableList()))
                        progressOff()
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