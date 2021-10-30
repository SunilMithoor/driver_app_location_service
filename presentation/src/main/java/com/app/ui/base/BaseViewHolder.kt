package com.app.ui.base

import android.content.Context
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.app.extension.ViewBindingVH
import com.app.extension.ItemClickListener
import com.app.extension.ItemLongClickListener
import com.app.extension.ItemViewClickListener
import kotlinx.android.extensions.LayoutContainer

abstract class BaseViewHolder<T>(private val view: View) : RecyclerView.ViewHolder(view),
    LayoutContainer {

    var itemClickListener: ItemClickListener<T>? = null
        get() = field
        set(value) {
            field = value
        }

    var itemLongClickListener: ItemLongClickListener<T>? = null
        get() = field
        set(value) {
            field = value
        }

    var itemViewClickListener: ItemViewClickListener<T>? = null
        get() = field
        set(value) {
            field = value
        }
    override val containerView: View?
        get() = view

    val context: Context
        get() = view.context

    abstract fun bind(position: Int, item: T)

}

abstract class BaseViewBinding<T>(val bind: ViewBinding) : ViewBindingVH(bind),
    LayoutContainer {

    var itemClickListener: ItemClickListener<T>? = null
        get() = field
        set(value) {
            field = value
        }

    var itemLongClickListener: ItemLongClickListener<T>? = null
        get() = field
        set(value) {
            field = value
        }

    var itemViewClickListener: ItemViewClickListener<T>? = null
        get() = field
        set(value) {
            field = value
        }
    override val containerView: View?
        get() = bind.root

    val context: Context
        get() = bind.root.context

    abstract fun bind(position: Int, item: T)
}