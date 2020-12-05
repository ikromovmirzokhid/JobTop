package com.imb.jobtop.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.imb.jobtop.R
import com.imb.jobtop.model.JobMinimal
import kotlinx.android.synthetic.main.list_item_job.view.*

class JobAdapter(private val onclickListener: OnJobClickListener) :
    ListAdapter<JobMinimal, JobViewHolder>(JobDiffCallback()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): JobViewHolder =
        JobViewHolder.from(parent)


    override fun onBindViewHolder(holder: JobViewHolder, position: Int) =
        holder.bind(getItem(position), onclickListener)

}

class JobViewHolder(itemView: View) :
    RecyclerView.ViewHolder(itemView) {

    @SuppressLint("SetTextI18n")
    fun bind(item: JobMinimal, onclickListener: OnJobClickListener) {
        itemView.postedTimeText.text = item.time.toString()
        itemView.jobTitleText.text = item.title
        itemView.jobEmployerText.text = item.employer
        itemView.jobLocationText.text = item.location
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

class JobDiffCallback : DiffUtil.ItemCallback<JobMinimal>() {
    override fun areItemsTheSame(oldItem: JobMinimal, newItem: JobMinimal): Boolean =
        oldItem.id == newItem.id

    override fun areContentsTheSame(oldItem: JobMinimal, newItem: JobMinimal): Boolean =
        oldItem.title == newItem.title && oldItem.location == newItem.location && oldItem.isFavorite == newItem.isFavorite
}

class OnJobClickListener(
    val onclickListener: (job: JobMinimal) -> Unit,
    val onFavoriteClickListener: (job: JobMinimal) -> Unit
) {
    fun onClick(job: JobMinimal) {
        onclickListener(job)
    }

    fun onFavorClick(job: JobMinimal) {
        onFavoriteClickListener(job)
    }

}

