package com.app.ui.dashboard

import android.os.Bundle
import com.app.R
import com.app.databinding.ActivityDashboardBinding
import com.app.extension.hideSupportActionBar
import com.app.extension.initBackToolbar
import com.app.extension.viewBinding
import com.app.ui.base.BaseAppCompatActivity
import com.app.vm.dashboard.DashboardVM
import org.koin.androidx.viewmodel.ext.android.viewModel


class DashboardActivity : BaseAppCompatActivity() {
    private val binding by viewBinding(ActivityDashboardBinding::inflate)

    private val dashboardVM by viewModel<DashboardVM>()

    override fun layout() = binding.root

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initialize()
    }

    private fun initialize() {
        initToolBar()
        initBubbleTabBar()
    }

    private fun initToolBar() {
        initBackToolbar(binding.toolBar)
        binding.toolBar.setNavigationOnClickListener {
            onSupportNavigateUp()
            binding.bubbleTabBar.setSelected(0)
            hideSupportActionBar()
        }
    }


    private fun initBubbleTabBar() {
        binding.bubbleTabBar.setSelected(0)
        binding.bubbleTabBar.addBubbleListener { id ->
            when (id) {
                R.id.home -> {
                    navController.navigate(R.id.homeFragment)
                }
                R.id.earnings -> {
                    navController.navigate(R.id.earningFragment)
                }
                R.id.account -> {
                    navController.navigate(R.id.accountFragment)
                }
            }
        }
    }

}