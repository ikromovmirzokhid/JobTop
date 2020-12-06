package com.imb.jobtop.model

import android.graphics.Bitmap

data class User (
    var id: String? = null,
    var name: String? = null,
    var bitmap: Bitmap? = null,
    var email: String? = null,
    var job: String? = null,
    var age: String? = null,
    var experience: String? = null,
    var interests: String? = null,
    var qualification: String? = null,
    var address: String? = null,
    var skills: String? = null,
    var moreDetails: String? = null,
    val favorites: List<String>? = null
)