package com.imb.jobtop.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.imb.jobtop.model.Job
import com.imb.jobtop.network.api.VacancyApi
import com.imb.jobtop.viewmodel.base.BaseViewModel
import javax.inject.Inject

//class JobViewModel @Inject constructor(
//    private val api: VacancyApi,
//    datasetID: Long,
//    vacancyID: Long
//) :
//    BaseViewModel() {
//    lateinit var job: Job
//
//    private val _submit = MutableLiveData<Boolean>()
//    val submit: LiveData<Boolean>
//        get() = _submit
//
//    fun submitClicked() {
//        _submit.value = true
//    }
//
//    fun submitCompleted() {
//        _submit.value = false
//    }
//}