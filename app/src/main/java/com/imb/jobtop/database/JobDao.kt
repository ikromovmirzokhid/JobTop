package com.imb.jobtop.database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.imb.jobtop.model.Job

@Dao
interface JobDao {

    @Insert
    fun insert(note: Job): Long

    @Update
    fun update(note: Job)

    @Delete
    fun delete(note: Job)

    @Query("SELECT * FROM Jobs WHERE id = :id")
    fun get(id: Long): Job?

    @Query("SELECT * FROM Jobs ORDER BY time DESC")
    fun getJobs(): List<Job>

    @Query("SELECT * FROM Jobs WHERE isFavorite == 1")
    fun getFavoriteJobs(): List<Job>

    @Query("SELECT * FROM Jobs WHERE catId == :catID")
    fun getJobsByCategory(catID: Long): List<Job>

    @Query("SELECT * FROM Jobs WHERE location LIKE :location")
    fun getJobsByLocation(location: String): List<Job>

    @Query("DELETE FROM Jobs")
    fun clear()

}