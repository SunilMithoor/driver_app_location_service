package com.app.ui.base

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView


abstract class BaseFooterPagedListAdapter<VH : RecyclerView.ViewHolder, FH : RecyclerView.ViewHolder, T>(
    callback: DiffUtil.ItemCallback<T>
) :
    BaseListAdapter<VH, T>(callback) {

    private var count = 0
    var useFooter = true
        private set
    private val ITEM = 0
    private val LOADING = 1
    private var isLoadMore = true
    private var loadMore: (() -> Unit?)? = null

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == LOADING) {
            onCreateFooterViewHolder(viewGroup, viewType)
        } else {
            onCreateItemViewHolder(viewGroup, viewType)
        }
    }

    override fun getItemCount(): Int {
        count = super.getItemCount()
        if (useFooter && count > 0) {
            count += 1
        }
        return count
    }

    override fun getItemViewType(position: Int): Int {
        if (position >= super.getItemCount() && useFooter) {
            return LOADING
        }
        return ITEM
    }

    @Suppress("UNCHECKED_CAST")
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (position == super.getItemCount() && holder.itemViewType == LOADING) {
            onBindFooterViewHolder(holder as FH, position)
        } else {
            onBindItemViewHolder(holder as VH, position)
        }
    }

    fun showLoader() {
        useFooter = true
        notifyDataSetChanged()
    }

    fun hideLoader() {
        useFooter = false
        notifyDataSetChanged()
    }

    abstract fun onCreateFooterViewHolder(viewGroup: ViewGroup, viewType: Int): FH

    abstract fun onBindFooterViewHolder(holder: FH, position: Int)
}