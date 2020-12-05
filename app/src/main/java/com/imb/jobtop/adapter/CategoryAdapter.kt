package com.imb.jobtop.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.imb.jobtop.R
import com.imb.jobtop.databinding.ListItemCategoryBinding
import com.imb.jobtop.model.Category
import kotlinx.android.synthetic.main.list_item_category.view.*

class CategoryAdapter(private val onclickListener: OnCategoryClickListener) :
    ListAdapter<Category, CategoryViewHolder>(CategoryDiffCallback()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder =
        CategoryViewHolder.from(parent)


    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) =
        holder.bind(getItem(position), onclickListener)
}

class CategoryViewHolder(private val binding: ListItemCategoryBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(item: Category, onclickListener: OnCategoryClickListener) {
        binding.category = item
        binding.categoryIcon.setImageBitmap(item.icon)
        binding.root.setOnClickListener { onclickListener.onClick(item) }
    }

    companion object {
        fun from(parent: ViewGroup): CategoryViewHolder {
            val binding = DataBindingUtil.inflate<ListItemCategoryBinding>(
                LayoutInflater.from(parent.context),
                R.layout.list_item_job,
                parent,
                false
            )
            return CategoryViewHolder(binding)
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