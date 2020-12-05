package com.imb.jobtop.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.imb.jobtop.R
import com.imb.jobtop.databinding.ListItemJobBinding
import com.imb.jobtop.model.JobMinimal

class JobAdapter(private val onclickListener: OnJobClickListener) :
    ListAdapter<JobMinimal, JobViewHolder>(JobDiffCallback()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): JobViewHolder =
        JobViewHolder.from(parent)


    override fun onBindViewHolder(holder: JobViewHolder, position: Int) =
        holder.bind(getItem(position), onclickListener)

}

class JobViewHolder(private val binding: ListItemJobBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(item: JobMinimal, onclickListener: OnJobClickListener) {
        binding.job = item
        binding.postedTimeText.text = item.time.toString()
        binding.root.setOnClickListener { onclickListener.onClick(item) }
        binding.favoriteBtn.setOnClickListener { onclickListener.onFavorClick(item) }
    }

    companion object {
        fun from(parent: ViewGroup): JobViewHolder {
            val binding = DataBindingUtil.inflate<ListItemJobBinding>(
                LayoutInflater.from(parent.context),
                R.layout.list_item_job,
                parent,
                false
            )
            return JobViewHolder(binding)
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

