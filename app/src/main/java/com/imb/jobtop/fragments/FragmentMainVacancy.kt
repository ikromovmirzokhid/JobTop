package com.imb.jobtop.fragments

import android.os.Bundle
import android.view.View
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.imb.jobtop.R
import com.imb.jobtop.adapter.CategoryAdapter
import com.imb.jobtop.adapter.JobAdapter
import com.imb.jobtop.adapter.OnCategoryClickListener
import com.imb.jobtop.adapter.OnJobClickListener
import com.imb.jobtop.di.components.MainComponent
import com.imb.jobtop.fragments.base.BaseFragment
import com.imb.jobtop.utils.extensions.progressOff
import com.imb.jobtop.utils.extensions.progressOn
import com.imb.jobtop.viewmodel.VacancyViewModel
import kotlinx.android.synthetic.main.fragment_main_vacancy.*

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

        })

        jobAdapter = JobAdapter(OnJobClickListener({

        }, {

        }))

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                TODO("Not yet implemented")
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                TODO("Not yet implemented")
            }
        })

        val jobManager = LinearLayoutManager(context)
        val catManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        jobList.adapter = jobAdapter
        jobList.layoutManager = jobManager
        categoryList.adapter = categoryAdapter
        categoryList.layoutManager = catManager
        loadVacancies()
    }

    private fun loadVacancies() {
        progressOn()
        viewModel.getVacancyFromSomeWhere().observe(viewLifecycleOwner, Observer {
            progressOff()
//            jobAdapter.submitList(it)
        })
    }
}