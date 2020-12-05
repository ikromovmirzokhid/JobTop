package com.imb.jobtop.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.imb.jobtop.R
import com.imb.jobtop.model.Category
import kotlinx.android.synthetic.main.list_item_category.view.*
import kotlinx.android.synthetic.main.list_item_job.view.*

class CategoryAdapter(private val onclickListener: OnCategoryClickListener) :
    ListAdapter<Category, CategoryViewHolder>(CategoryDiffCallback()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder =
        CategoryViewHolder.from(parent)


    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) =
        holder.bind(getItem(position), onclickListener)
}

class CategoryViewHolder(private val itemView: View) :
    RecyclerView.ViewHolder(itemView) {

    @SuppressLint("SetTextI18n")
    fun bind(item: Category, onclickListener: OnCategoryClickListener) {
        itemView.categoryTitle.text = item.title
        itemView.categoryCount.text = "${item.jobCount} ta ish o'rni mavjud"
//        itemView.categoryIcon.setImageBitmap(item.icon)
        itemView.setOnClickListener { onclickListener.onClick(item) }
    }

    companion object {
        fun from(parent: ViewGroup): CategoryViewHolder {
            val v = LayoutInflater.from(parent.context).inflate(
                R.layout.list_item_job,
                parent,
                false
            )
            return CategoryViewHolder(v)
        }
    }
}

class CategoryDiffCallback : DiffUtil.ItemCallback<Category>() {
    override fun areItemsTheSame(oldItem: Category, newItem: Category): Boolean =
        oldItem.id == newItem.id

    override fun areContentsTheSame(oldItem: Category, newItem: Category): Boolean =
        oldItem.title == oldItem.title && oldItem.jobCount == newItem.jobCount
}

class OnCategoryClickListener(val onclickListener: (cat: Category) -> Unit) {
    fun onClick(cat: Category) {
        onclickListener(cat)
    }
}