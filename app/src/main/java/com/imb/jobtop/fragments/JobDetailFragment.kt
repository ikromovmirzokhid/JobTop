package com.imb.jobtop.fragments

import android.os.Bundle
import android.view.View
import com.imb.jobtop.R
import com.imb.jobtop.di.components.MainComponent
import com.imb.jobtop.fragments.base.BaseFragment
import com.imb.jobtop.model.Job
import com.imb.jobtop.utils.extensions.getData
import kotlinx.android.synthetic.main.fragment_job_detail.*

class JobDetailFragment : BaseFragment(R.layout.fragment_job_detail) {

    private val component by lazy {
        MainComponent.create()
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val job: Job? = arguments?.getData<Job>("job")
        job?.let {
            jobTitle.text = job.title
            jobEmployer.text = job.employer
            jobInfo.text = job.info
            jobSalary.text = job.salary
            jobLocation.text = job.location
            jobPostTime.text = job.time.toString()
            jobRequirements.text = job.requirements.joinToString(separator = "\n")
        }

        submitBtn.setOnClickListener {
            TODO("submit CV")
        }
    }
}