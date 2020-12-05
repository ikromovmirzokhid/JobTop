package com.imb.jobtop.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.imb.jobtop.R
import com.imb.jobtop.adapter.CategoryAdapter
import com.imb.jobtop.adapter.JobAdapter
import com.imb.jobtop.databinding.FragmentJobBinding
import com.imb.jobtop.databinding.FragmentMainVacancyBinding
import com.imb.jobtop.di.components.MainComponent
import com.imb.jobtop.fragments.base.BaseFragment
import com.imb.jobtop.viewmodel.JobViewModel
import com.imb.jobtop.viewmodel.VacancyViewModel

class JobFragment : BaseFragment(R.layout.fragment_job) {

    private val component by lazy {
        MainComponent.create()
    }

    private val viewModel by viewModels<JobViewModel> { component.viewModelFactory() }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val binding = FragmentJobBinding.inflate(layoutInflater)

        viewModel.submit.observe(viewLifecycleOwner, {
            if (it) {

                viewModel.submitCompleted()
            }
        })

        binding.viewModel = viewModel
    }
}