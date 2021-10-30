package com.app.ui.base

import android.annotation.SuppressLint
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.app.extension.ItemClickListener
import com.app.extension.ItemLongClickListener
import com.app.extension.ItemViewClickListener
import com.app.extension.runOnUiThread


abstract class BaseListAdapter<VH : RecyclerView.ViewHolder, T>(callback: DiffUtil.ItemCallback<T>) :
    ListAdapter<T, RecyclerView.ViewHolder>(callback) {
    internal var itemClickListener: ItemClickListener<T>? = null
    internal var itemLongClickListener: ItemLongClickListener<T>? = null
    internal var itemViewClickListener: ItemViewClickListener<T>? = null
    private var loadMore: (() -> Unit?)? = null
    private var isLoadMore = false

    fun loadMore(f: () -> Unit) {
        enableLoadMore()
        loadMore = f
    }

    fun enableLoadMore() {
        isLoadMore = true
    }

    fun disableLoadMore() {
        isLoadMore = false
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return onCreateItemViewHolder(viewGroup, viewType)
    }

    @Suppress("UNCHECKED_CAST")
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (position == itemCount - 2 && isLoadMore) {
            runOnUiThread {
                loadMore?.invoke()
            }
        }
        onBindItemViewHolder(holder as VH, position)
    }

    fun click(f: ItemClickListener<T>) {
        itemClickListener = f
    }

    fun longClick(f: ItemLongClickListener<T>) {
        itemLongClickListener = f
    }

    fun iconClick(f: ItemViewClickListener<T>) {
        itemViewClickListener = f
    }

    abstract fun onCreateItemViewHolder(viewGroup: ViewGroup, viewType: Int): VH

    abstract fun onBindItemViewHolder(holder: VH, position: Int)
}

class DifCallback<T> :
    DiffUtil.ItemCallback<T>() {

    override fun areItemsTheSame(oldItem: T, newItem: T): Boolean {
        return oldItem === newItem
    }

    @SuppressLint("DiffUtilEquals")
    override fun areContentsTheSame(oldItem: T, newItem: T): Boolean {
        return oldItem == newItem
    }

}

