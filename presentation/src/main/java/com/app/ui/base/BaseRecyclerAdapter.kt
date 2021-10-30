package com.app.ui.base

import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.extensions.LayoutContainer


abstract class BaseRecyclerAdapter<VH : RecyclerView.ViewHolder, T> :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    internal var count = 0
    internal var clickListener: ((Int, T) -> Unit?)? = null
    internal var longClickListener: ((Int, T) -> Unit?)? = null
    internal var iconClickListener: ((Int, Int, T) -> Unit?)? = null
    internal val clickPosition = MutableLiveData<Item<T>>()


    var list = mutableListOf<T>()
        set(items) {
            val diffCallback = DiffCallback(items, field)
            val diffResult = DiffUtil.calculateDiff(diffCallback)
            field = items
            count = list.size
            this.notifyDataSetChanged()
            //diffResult.dispatchUpdatesTo(this)
        }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return onCreateItemViewHolder(viewGroup, viewType)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    @Suppress("UNCHECKED_CAST")
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        onBindItemViewHolder(holder as VH, position)
    }

    fun getItem(position: Int): T {
        return list[position]
    }

    fun getCount(): Int {
        return list.size
    }

    fun addItem(name: T) {
        list.add(name)
        notifyItemInserted(list.size)
    }

    fun removeAt(position: Int) {
        list.removeAt(position)
        notifyItemRemoved(position)
    }

    fun restoreAt(position: Int, t: T) {
        list.add(position, t)
        notifyItemInserted(position)
    }

    fun click(f: (Int, T) -> Unit) {
        clickListener = f
    }

    fun longClick(f: (Int, T) -> Unit) {
        longClickListener = f
    }

    fun iconClick(f: (Int, Int, T) -> Unit) {
        iconClickListener = f
    }

    abstract fun onCreateItemViewHolder(viewGroup: ViewGroup, viewType: Int): VH

    abstract fun onBindItemViewHolder(holder: VH, position: Int)
}

data class Item<T>(
    val position: Int,
    val model: T
)

class Holder(view: View) : RecyclerView.ViewHolder(view)
abstract class HolderContainer<T>(val view: View) : RecyclerView.ViewHolder(view), LayoutContainer {

    override val containerView: View?
        get() = view

    abstract fun bind(position: Int, item: T)
}

private class DiffCallback<T>(private val newList: List<T>, private val oldList: List<T>) :
    DiffUtil.Callback() {

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition] === newList[newItemPosition]
    }

    override fun getOldListSize(): Int {
        return oldList.size
    }

    override fun getNewListSize(): Int {
        return newList.size
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition] == newList[newItemPosition]
    }

}
