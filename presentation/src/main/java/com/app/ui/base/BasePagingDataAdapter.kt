package com.app.ui.base

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.app.databinding.ItemLoadStateBinding
import com.app.extension.ItemClickListener
import com.app.extension.ItemLongClickListener
import com.app.extension.ItemViewClickListener

abstract class BasePagingDataAdapter<T : Any, VH : RecyclerView.ViewHolder>(callback: DiffUtil.ItemCallback<T>) :
    PagingDataAdapter<T, VH>(callback) {

    internal var itemClickListener: ItemClickListener<T>? = null
    internal var itemLongClickListener: ItemLongClickListener<T>? = null
    internal var itemViewClickListener: ItemViewClickListener<T>? = null

    fun itemClick(f: ItemClickListener<T>) {
        itemClickListener = f
    }

    fun itemLongClick(f: ItemLongClickListener<T>) {
        itemLongClickListener = f
    }

    fun itemViewClick(f: ItemViewClickListener<T>) {
        itemViewClickListener = f
    }
}

class BaseLoadStateAdapter : LoadStateAdapter<Holder>() {
    override fun onBindViewHolder(holder: Holder, loadState: LoadState) {
        val binding = ItemLoadStateBinding.bind(holder.itemView)
        when (loadState) {
            is LoadState.Loading -> {
                binding.progress.visibility = View.VISIBLE
            }
            is LoadState.Error -> {
                binding.progress.visibility = View.GONE
            }
            is LoadState.NotLoading -> {
                binding.progress.visibility = View.GONE
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, loadState: LoadState): Holder {
        return Holder(
            ItemLoadStateBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ).root
        )
    }
}