package com.imb.jobtop.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.imb.jobtop.R
import com.imb.jobtop.model.Job
import kotlinx.android.synthetic.main.list_item_job.view.*

class JobAdapter(private val onclickListener: OnJobClickListener) :
    ListAdapter<Job, JobViewHolder>(JobDiffCallback()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): JobViewHolder =
        JobViewHolder.from(parent)


    override fun onBindViewHolder(holder: JobViewHolder, position: Int) =
        holder.bind(getItem(position), onclickListener)

}

class JobViewHolder(itemView: View) :
    RecyclerView.ViewHolder(itemView) {

    @SuppressLint("SetTextI18n")
    fun bind(item: Job, onclickListener: OnJobClickListener) {
        itemView.jobTitleText.text = item.title
        itemView.jobEmployerText.text = item.employer
        itemView.jobLocationText.text = item.location.subSequence(
            0,
            item.location.indexOf(" ")
        )
        itemView.jobSalaryText.text = "Maosh - ${item.salary}"
        itemView.setOnClickListener { onclickListener.onClick(item) }
        itemView.favoriteBtn.setOnClickListener { onclickListener.onFavorClick(item) }
    }

    companion object {
        fun from(parent: ViewGroup): JobViewHolder {
            val v = LayoutInflater.from(parent.context).inflate(
                R.layout.list_item_job,
                parent,
                false
            )
            return JobViewHolder(v)
        }
    }
}

class JobDiffCallback : DiffUtil.ItemCallback<Job>() {
    override fun areItemsTheSame(oldItem: Job, newItem: Job): Boolean =
        oldItem.id == newItem.id

    override fun areContentsTheSame(oldItem: Job, newItem: Job): Boolean =
        oldItem.title == newItem.title && oldItem.location == newItem.location && oldItem.isFavorite == newItem.isFavorite
}

class OnJobClickListener(
    val onclickListener: (job: Job) -> Unit,
    val onFavoriteClickListener: (job: Job) -> Unit
) {
    fun onClick(job: Job) {
        onclickListener(job)
    }

    fun onFavorClick(job: Job) {
        onFavoriteClickListener(job)
    }

}

