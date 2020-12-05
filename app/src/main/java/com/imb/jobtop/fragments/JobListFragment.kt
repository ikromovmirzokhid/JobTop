package com.imb.jobtop.fragments

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.imb.jobtop.R
import com.imb.jobtop.adapter.JobAdapter
import com.imb.jobtop.adapter.OnJobClickListener
import com.imb.jobtop.database.MyDatabase
import com.imb.jobtop.fragments.base.BaseFragment
import com.imb.jobtop.model.Job
import com.imb.jobtop.utils.extensions.getData
import com.imb.jobtop.utils.extensions.putData
import kotlinx.android.synthetic.main.fragment_job_list.*

class JobListFragment : BaseFragment(R.layout.fragment_job_list) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val type = arguments?.getInt("type")
        val db = MyDatabase.getInstance(requireContext())
        val list: MutableList<Job>?
        when (type) {
            0 -> list = db.jodDao.getJobs().toMutableList()

            1 -> list = db.jodDao.getJobs().toMutableList()

            2 -> list = db.jodDao.getJobs().toMutableList()
        }


        val jobAdapter = JobAdapter(OnJobClickListener({
            val b = Bundle()
            b.putData("job", it)
            findNavController().navigate(R.id.jobDetailFragment, b)
        }, {
            it.isFavorite = !it.isFavorite
            db.jodDao.update(it)
        }))

        jobList.adapter = jobAdapter
        jobList.layoutManager = LinearLayoutManager(context)

    }
}