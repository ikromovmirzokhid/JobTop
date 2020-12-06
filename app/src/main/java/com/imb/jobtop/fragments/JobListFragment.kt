package com.imb.jobtop.fragments

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import com.imb.jobtop.R
import com.imb.jobtop.adapter.JobAdapter
import com.imb.jobtop.adapter.OnJobClickListener
import com.imb.jobtop.fragments.base.BaseFragment
import com.imb.jobtop.model.*
import com.imb.jobtop.utils.extensions.getData
import com.imb.jobtop.utils.extensions.getList
import com.imb.jobtop.utils.extensions.putData
import kotlinx.android.synthetic.main.fragment_job_list.*

class JobListFragment : BaseFragment(R.layout.fragment_job_list) {

    private var data: MutableList<Job>? = mutableListOf()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val db = Firebase.firestore
        val type = arguments?.getInt("key")
        var list: MutableList<String>? = mutableListOf()
        data?.clear()
        if (type == 1)
            list = arguments?.getData<Strings>("data_list")!!.list
        else
            data = arguments?.getData<Jobs>("data_list")!!.list
        val favList = mutableListOf<Job>()

        Log.d("TAG", "aaa: ${data?.size}} ")
        val jobAdapter = JobAdapter(OnJobClickListener({
            val b = Bundle()
            b.putData("job", it)
            findNavController().navigate(R.id.jobDetailFragment, b)
        }, {
            val user = db.collection("users").document(Firebase.auth.currentUser!!.uid)
            it.isFavorite = !it.isFavorite
            if (it.isFavorite) {
                user.update(
                    "favorites",
                    FieldValue.arrayUnion("${it.catId} ${it.id}")
                )
            } else {
                user.update(
                    "favorites",
                    FieldValue.arrayRemove("${it.catId} ${it.id}")
                )
            }
        }))
        jobAdapter.submitList(data)
        if (list != null) {
            for (i in list) {
                val splitted = i.split(" ")
                db.collection(splitted[0]).document(splitted[1]).get()
                    .addOnSuccessListener { j ->
                        val job = j.toObject<Vacancy>()!!
                        data!!.add(Mapper.vacancyToJob(job, j.id, splitted[0], true))
                        Log.d("TAG", "favlist: $favList")
                        jobAdapter.submitList(data)
                        jobAdapter.notifyDataSetChanged()
                    }
                Log.d("TAG", "favlist: $favList")
                Log.d("TAG", "splitted: $splitted")
            }
        }

        buttonBackArrow.setOnClickListener {
            pressBack()
        }
        jobList.adapter = jobAdapter
        jobList.layoutManager = LinearLayoutManager(context)
    }
}