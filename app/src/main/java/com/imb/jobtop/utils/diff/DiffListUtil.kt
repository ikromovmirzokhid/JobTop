package com.imb.jobtop.utils.diff

import androidx.recyclerview.widget.DiffUtil

class DiffListUtil<T : Any>(private var oldList: List<T>, private var newList: List<T>) : DiffUtil.Callback() {
    override fun areItemsTheSame(p0: Int, p1: Int) = oldList[p0].javaClass.canonicalName == newList[p1].javaClass.name

    override fun getOldListSize() = oldList.size

    override fun getNewListSize() = newList.size

    override fun areContentsTheSame(p0: Int, p1: Int) = oldList[p0].hashCode() == newList[p1].hashCode()
}