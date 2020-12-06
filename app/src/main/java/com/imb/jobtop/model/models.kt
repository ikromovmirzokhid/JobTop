package com.imb.jobtop.model

data class Job(
    var id: String,
    var catId: String,
    var title: String,
    var employer: String,
    var phoneNumber: String,
    var salary: String,
    var isFavorite: Boolean = false,
    var location: String,
    var requirements: String
)

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
    fun vacancyToJob(v: Vacancy, id: String, catID: String): Job {
        return Job(
            id = id,
            catId = catID,
            title = v.lavozim ?: "oddiy ishchi",
            employer = v.tashkilot_nomi ?: "Nomi yoq",
            phoneNumber = v.tel ?: "000",
            salary = v.maosh ?: "tekinga",
            location = v.address ?: "oshatta",
            requirements = v.talablar ?: "diplom bolsa boldi"
        )
    }
}

data class Jobs(
    val list: MutableList<Job>
)