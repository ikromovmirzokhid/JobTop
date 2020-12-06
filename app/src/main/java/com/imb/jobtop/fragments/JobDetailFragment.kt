package com.imb.jobtop.fragments

import android.os.Bundle
import android.view.View
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
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
        val job: Job = arguments?.getData<Job>("job")!!
        jobTitle.text = job.title
        jobEmployer.text = job.employer
        jobInfo.text = job.location
        jobSalary.text = job.salary
        jobLocationMin.text = job.location.subSequence(0, job.location.indexOf(","))
        jobEmployerNumber.text = job.phoneNumber
        jobRequirements.text = job.requirements

        val db = Firebase.firestore
        favBtn.setOnClickListener {
            val user = db.collection("users").document(Firebase.auth.currentUser!!.uid)
            job.isFavorite = !job.isFavorite
            if (job.isFavorite) {
                user.update(
                    "favorites",
                    FieldValue.arrayUnion("${job.catId} ${job.id}")
                )
            } else {
                user.update(
                    "favorites",
                    FieldValue.arrayRemove("${job.catId} ${job.id}")
                )
            }
        }
        drawerBtn.setOnClickListener {
            pressBack()
        }
        submitBtn.setOnClickListener {
            TODO("submit CV")
        }
    }
}