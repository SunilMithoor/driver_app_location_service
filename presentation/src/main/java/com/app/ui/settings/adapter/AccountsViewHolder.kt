package com.app.ui.settings.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.app.databinding.AccountsItemBinding
import com.app.extension.click
import com.app.extension.drawableStart
import com.app.extension.resString
import com.app.utilities.NavigationDrawer
import com.app.ui.base.BaseViewHolder


class AccountsViewHolder(val view: View) : BaseViewHolder<NavigationDrawer>(view) {

    override fun bind(position: Int, item: NavigationDrawer) {
        val binding = AccountsItemBinding.bind(view)
        binding.tvName.resString = item.stringRes
        binding.tvName.drawableStart = item.resId
        binding.root.click {
            itemClickListener?.invoke(position, item)
        }
    }

    companion object {
        fun create(parent: ViewGroup): AccountsViewHolder {
            return AccountsViewHolder(
                AccountsItemBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                ).root
            )
        }
    }

}