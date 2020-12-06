package com.imb.jobtop.fragments

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import com.imb.jobtop.R
import com.imb.jobtop.adapter.CategoryAdapter
import com.imb.jobtop.adapter.JobAdapter
import com.imb.jobtop.adapter.OnCategoryClickListener
import com.imb.jobtop.adapter.OnJobClickListener
import com.imb.jobtop.di.components.MainComponent
import com.imb.jobtop.fragments.base.BaseFragment
import com.imb.jobtop.model.*
import com.imb.jobtop.utils.extensions.progressOff
import com.imb.jobtop.utils.extensions.progressOn
import com.imb.jobtop.utils.extensions.putData
import com.imb.jobtop.viewmodel.VacancyViewModel
import kotlinx.android.synthetic.main.fragment_job_detail.*
import kotlinx.android.synthetic.main.fragment_main_vacancy.*
import kotlinx.android.synthetic.main.fragment_main_vacancy.drawerBtn

class FragmentMainVacancy : BaseFragment(R.layout.fragment_main_vacancy) {

    private val component by lazy {
        MainComponent.create()
    }
    private lateinit var jobAdapter: JobAdapter
    private lateinit var categoryAdapter: CategoryAdapter

    private lateinit var db: FirebaseFirestore
    private lateinit var data: MutableList<Job>
    private var interest: String? = null
    private lateinit var dataByInterest: MutableList<Job>
    private lateinit var categ: MutableList<Category>
    private val viewModel by viewModels<VacancyViewModel> { component.viewModelFactory() }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        db = MyDatabase.getInstance(requireContext())
        db = Firebase.firestore
        data = mutableListOf()
        categ = mutableListOf()
        db.collection("user").document(Firebase.auth.uid!!).get().addOnSuccessListener {
            interest = it.toObject<User>()?.interests
            fetchData("energetika")
            fetchData("ta'lim")
            fetchData("iqtisod")
            fetchData("tibbiyot")
            fetchData("transport")
        }

        initAdapters()
        initSearchView()
        initNavBar()
        initButtonClickListeners()
        Log.d("TAG", "onViewCreated: ${data.size} $data ")

        jobAdapter.submitList(data)
        categoryAdapter.submitList(categ)
        jobList.adapter = jobAdapter
        jobList.layoutManager = LinearLayoutManager(context)
        categoryList.adapter = categoryAdapter
        categoryList.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        Log.d("TAG", "onViewCreated: ${jobAdapter.currentList.size} ${jobAdapter.currentList} ")
    }

    private fun initButtonClickListeners() {
        seeAllBtn.setOnClickListener {
            val b = Bundle()
            b.putData("data_list", Jobs(data))
            findNavController().navigate(R.id.jobList, b)
        }
        mapBtn.setOnClickListener {
            val b = Bundle()
            b.putData("data_list", Jobs(data))
            findNavController().navigate(R.id.fragmentMapJobs, b)
        }
    }


    private fun fetchData(cat: String) {
        db.collection(cat).get().addOnSuccessListener { document ->
            if (document != null) {
                for (doc in document) {
                    val d = doc.toObject<Vacancy>()
                    data.add(Mapper.vacancyToJob(d, doc.id, cat))
                    interest?.let {
                        if (doc.id == interest)
                            dataByInterest.add(Mapper.vacancyToJob(d, doc.id, cat))
                    }
                    Log.d("TAG", "fetchData: ${doc.id} ${doc.data}")
                    Log.d("TAG", "fetchedData: $d")
                }
                categ.add(Category(db.collection(cat).id, cat, document.documents.size))
            }
            jobAdapter.submitList(dataByInterest)
            jobAdapter.notifyDataSetChanged()
            categoryAdapter.submitList(categ)
            categoryAdapter.notifyDataSetChanged()
        }.addOnFailureListener {
            Log.d("TAG", "fetchData: ${it.message}")
        }
    }

    private fun initAdapters() {
        categoryAdapter = CategoryAdapter(OnCategoryClickListener {
            val b = Bundle()
            val catData = mutableListOf<Job>()
            for (d in data) {
                if (d.catId == it.id)
                    catData.add(d)
            }
            b.putData("data_list", Jobs(catData))
            findNavController().navigate(R.id.jobListFragment, b)
        })
        jobAdapter = JobAdapter(OnJobClickListener({
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
    }

    private fun initSearchView() {
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if (!newText.isNullOrEmpty()) {
                    val newData = mutableListOf<Job>()
                    for (d in data) {
                        if (d.location.contains(newText, true))
                            newData.add(d)
                    }
                    jobAdapter.submitList(newData)
                } else jobAdapter.submitList(data)
                return true
            }
        })
    }

    private fun initNavBar() {
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