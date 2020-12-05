package com.imb.jobtop.fragments

import android.os.Bundle
import android.view.View
import androidx.appcompat.widget.SearchView
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.NavigationUI
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.navigation.NavigationView
import com.imb.jobtop.R
import com.imb.jobtop.adapter.CategoryAdapter
import com.imb.jobtop.adapter.JobAdapter
import com.imb.jobtop.adapter.OnCategoryClickListener
import com.imb.jobtop.adapter.OnJobClickListener
import com.imb.jobtop.di.components.MainComponent
import com.imb.jobtop.fragments.base.BaseFragment
import com.imb.jobtop.utils.extensions.progressOff
import com.imb.jobtop.utils.extensions.progressOn
import com.imb.jobtop.utils.extensions.putData
import com.imb.jobtop.viewmodel.VacancyViewModel
import kotlinx.android.synthetic.main.fragment_main_vacancy.*
import java.nio.BufferUnderflowException

class FragmentMainVacancy : BaseFragment(R.layout.fragment_main_vacancy) {

    private val component by lazy {
        MainComponent.create()
    }
    private lateinit var jobAdapter: JobAdapter
    private lateinit var categoryAdapter: CategoryAdapter

    private val viewModel by viewModels<VacancyViewModel> { component.viewModelFactory() }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        categoryAdapter = CategoryAdapter(OnCategoryClickListener {
            val b = Bundle()
            b.putData("category_id", it)
            b.putInt("type", 0)
            findNavController().navigate(R.id.jobList, b)
        })

        jobAdapter = JobAdapter(OnJobClickListener({
            val b = Bundle()
            b.putData("job", it)
            findNavController().navigate(R.id.jobDetailFragment, b)
        }, {
            TODO("favourite button")
        }))

        initSearchView()
        initBottomNavBar()

        val jobManager = LinearLayoutManager(context)
        val catManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        jobList.adapter = jobAdapter
        jobList.layoutManager = jobManager
        categoryList.adapter = categoryAdapter
        categoryList.layoutManager = catManager
        loadVacancies()
    }

    private fun initSearchView() {
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                TODO("Not yet implemented")
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                TODO("Not yet implemented")
            }
        })
    }

    private fun initBottomNavBar() {
        bottomNavigation.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.homePage -> {
                    findNavController()
                }
                R.id.favPage -> {
                    val b = Bundle()
                    b.putInt("type", 1)
                    findNavController().navigate(R.id.jobListFragment, b)
                }
                R.id.userPage -> {

                }
            }
            true
        }
    }

    private fun initToolbar() {
        drawerBtn.setOnClickListener {
            if (drawerLayout != null && navView != null)
                drawerLayout.openDrawer(navView)//GravityCompat.START)
        }
        navView.setNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.myResume -> {
                }
                R.id.userPolicy -> {
                }
                R.id.about -> {
                }
                R.id.exit -> {
                    pressBack()
                }
            }
            true
        }
    }

    private fun loadVacancies() {
        progressOn()
        viewModel.getVacancyFromSomeWhere().observe(viewLifecycleOwner, Observer {
            progressOff()
//            jobAdapter.submitList(it)
        })
    }
}