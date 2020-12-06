package com.imb.jobtop.fragments

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import android.widget.Toast
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.imb.jobtop.R
import com.imb.jobtop.di.components.MainComponent
import com.imb.jobtop.fragments.base.BaseFragment
import com.imb.jobtop.model.Job
import com.imb.jobtop.utils.extensions.getData
import com.imb.jobtop.utils.extensions.progressOff
import com.imb.jobtop.utils.extensions.progressOn
import kotlinx.android.synthetic.main.fragment_job_detail.*
import me.echodev.resizer.Resizer
import java.io.File

class JobDetailFragment : BaseFragment(R.layout.fragment_job_detail) {

    private val component by lazy {
        MainComponent.create()
    }

    private val PICK_PDF_CODE = 3001


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val job: Job = arguments?.getData<Job>("job")!!
        jobTitle.text = job.jobTitle
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
            readPdfFilesFromInternalStorage()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, dataIntent: Intent?) {

        if (resultCode != Activity.RESULT_OK) {
            Log.d("TTT", "resultCode != Activity.RESULT_OK")
            return
        }
        if (requestCode == PICK_PDF_CODE) {
            val handler = Handler()
            progressOn()
            handler.postDelayed({
                progressOff()
                Snackbar.make(
                    requireView(),
                    "Rezyumingiz qabul qilindi, Javobi tez orada sizning emailingizga yuboriladi!",
                    Snackbar.LENGTH_SHORT
                ).show()
            }, 5000)
        }
        dataIntent?.data = null
    }

    private fun readPdfFilesFromInternalStorage() {
        val intentPDF = Intent(Intent.ACTION_GET_CONTENT)
        intentPDF.setType("application/pdf")
        intentPDF.addCategory(Intent.CATEGORY_OPENABLE)
        startActivityForResult(Intent.createChooser(intentPDF, "Select Picture"), PICK_PDF_CODE)
    }
}