package com.app.ui.settings.adapter

import android.view.ViewGroup
import com.app.utilities.NavigationDrawer
import com.app.ui.base.BaseListAdapter
import com.app.ui.base.DifCallback


class AccountsAdapter : BaseListAdapter<AccountsViewHolder, NavigationDrawer>(DifCallback()) {

    override fun onCreateItemViewHolder(viewGroup: ViewGroup, viewType: Int): AccountsViewHolder {
        val holder = AccountsViewHolder.create(viewGroup)
        holder.itemClickListener = itemClickListener
        return holder
    }


    override fun onBindItemViewHolder(holder: AccountsViewHolder, position: Int) {
        holder.bind(position, getItem(position))
    }
}