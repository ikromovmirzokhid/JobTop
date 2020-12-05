package com.imb.jobtop.model

import android.graphics.Bitmap
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Jobs")
data class Job(
    @PrimaryKey(autoGenerate = true)
    var id: Long,
    var catId: Long,
    var title: String,
    var employer: String,
    var info: String,
    var salary: String,
    var isFavorite: Boolean,
    var location: String,
    var time: Long,
    var requirements: List<String>
)

data class Category(
    var id: Long,
    var icon: Bitmap,
    var title: String,
    var jobCount: Int,
)