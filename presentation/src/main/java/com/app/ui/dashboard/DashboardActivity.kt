package com.app.ui.dashboard

import android.app.AppOpsManager
import android.content.res.Configuration
import android.os.Build
import android.os.Bundle
import android.os.Process
import com.app.R
import com.app.databinding.ActivityDashboardBinding
import com.app.extension.hideSupportActionBar
import com.app.extension.initBackToolbar
import com.app.extension.viewBinding
import com.app.ui.base.BaseAppCompatActivity
import com.app.vm.dashboard.DashboardVM
import org.koin.androidx.viewmodel.ext.android.viewModel
import timber.log.Timber


class DashboardActivity : BaseAppCompatActivity() {
    private val binding by viewBinding(ActivityDashboardBinding::inflate)
    private val dashboardVM by viewModel<DashboardVM>()
    override fun layout() = binding.root

    private var appOpsManager: AppOpsManager? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initialize()
    }

    override fun onResume() {
        super.onResume()
        if (disablePermissionPiPMode()) {
            Timber.d("Enable Pip")
        }
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

    override fun onPictureInPictureModeChanged(
        isInPictureInPictureMode: Boolean, configuration: Configuration?
    ) {
        super.onPictureInPictureModeChanged(isInPictureInPictureMode, configuration)
        if (isInPictureInPictureMode) {
            Timber.d("PIP mode")
        } else {
            Timber.d("NO PIP mode")
        }
    }


    private fun disablePermissionPiPMode(): Boolean {
        try {
            appOpsManager = getSystemService(APP_OPS_SERVICE) as AppOpsManager
            if (appOpsManager != null) {
                return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                        AppOpsManager.MODE_ERRORED == appOpsManager?.unsafeCheckOpNoThrow(
                            AppOpsManager.OPSTR_PICTURE_IN_PICTURE,
                            Process.myUid(), packageName
                        )
                    } else {
                        AppOpsManager.MODE_ERRORED == appOpsManager?.checkOpNoThrow(
                            AppOpsManager.OPSTR_PICTURE_IN_PICTURE,
                            Process.myUid(), packageName
                        )
                    }
                } else false
            }
        } catch (e: Exception) {
            e.printStackTrace()
            return false
        }
        return false
    }

}