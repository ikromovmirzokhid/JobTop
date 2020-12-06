package com.imb.jobtop.model

import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.clustering.ClusterItem

data class Job(
    var id: String,
    var catId: String,
    var jobTitle: String,
    var employer: String,
    var phoneNumber: String,
    var salary: String,
    var isFavorite: Boolean = false,
    var location: String,
    var requirements: String,
    val lat: String? = null,
    val lng: String? = null
) : ClusterItem {
    override fun getPosition(): LatLng {
        return LatLng(lat?.toDouble() ?: 0.0, lng?.toDouble() ?: 0.0)
    }

    override fun getTitle(): String? {
        return jobTitle
    }

    override fun getSnippet(): String? {
        return "snippet"
    }

}

data class Category(
    var id: String,
    var title: String = "Title",
    var jobCount: Int = 0
)

data class Vacancy(
    val address: String? = null,
    val lat: String? = null,
    val lng: String? = null,
    val lavozim: String? = null,
    val maosh: String? = null,
    val talablar: String? = null,
    val tashkilot_nomi: String? = null,
    val tel: String? = null
)

//data class User(
//    val interests: String? = null,
//    val name: String? = null,
//
//)

object Mapper {
    fun vacancyToJob(v: Vacancy, id: String, catID: String, b: Boolean = false): Job {
        return Job(
            id = id,
            isFavorite = b,
            catId = catID,
            jobTitle = v.lavozim ?: "oddiy ishchi",
            employer = v.tashkilot_nomi ?: "Nomi yoq",
            phoneNumber = v.tel ?: "000",
            salary = v.maosh ?: "tekinga",
            location = v.address ?: "oshatta",
            requirements = v.talablar ?: "diplom bolsa boldi",
            lat = v.lat,
            lng = v.lng,
        )
    }
}

data class Jobs(
    val list: MutableList<Job>
)

data class Strings(
    val list: MutableList<String>?
)