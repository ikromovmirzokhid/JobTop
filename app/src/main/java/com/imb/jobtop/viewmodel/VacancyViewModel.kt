package com.imb.jobtop.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.imb.jobtop.network.api.VacancyApi
import com.imb.jobtop.utils.resources.ResourceProvider
import com.imb.jobtop.viewmodel.base.BaseViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.HttpException
import javax.inject.Inject

class VacancyViewModel @Inject constructor(
    private val resources: ResourceProvider,
    private val api: VacancyApi
) : BaseViewModel() {

    private var _data: MutableLiveData<Any>? = null
    private val data: LiveData<Any>?
        get() = _data

    fun getVacancyFromSomeWhere(): LiveData<Any> {
        _data = MutableLiveData()
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = api.getEconomyVacancy()
                _data?.postValue(response)
            } catch (error: HttpException) {

            } catch (error: Throwable) {

            }
        }
        return data!!
    }

}