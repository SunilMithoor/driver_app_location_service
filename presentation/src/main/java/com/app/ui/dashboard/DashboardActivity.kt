package com.app.ui.dashboard

import android.app.Activity
import android.content.Intent
import android.content.res.Configuration
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.view.View
import com.app.R
import com.app.databinding.ActivityDashboardBinding
import com.app.extension.*
import com.app.ui.base.BaseAppCompatActivity
import com.app.ui.splash.SplashActivity
import com.app.utilities.PERMISSION_REQUEST_CODE
import com.app.vm.dashboard.DashboardVM
import org.koin.androidx.viewmodel.ext.android.viewModel
import timber.log.Timber


class DashboardActivity : BaseAppCompatActivity() {
    private val binding by viewBinding(ActivityDashboardBinding::inflate)
    private val dashboardVM by viewModel<DashboardVM>()
    override fun layout() = binding.root


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initialize()
    }

    override fun onResume() {
        super.onResume()
        if (this.checkPiPModePermission()) {
            pipPermissionDialog()
        }
    }

    private fun pipPermissionDialog() {
        alert(R.style.Dialog_Alert) {
            setCancelable(false)
            setTitle(AppString.permissions_required)
            setMessage(AppString.pip_perm_msg_alert)
            setPositiveButton(AppString.label_ok) { _, _ ->
                val i = Intent(
                    Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.fromParts(
                        "package",
                        packageName, null
                    )
                )
                startActivityForResult(i, PERMISSION_REQUEST_CODE)
            }
            setNegativeButton(AppString.label_cancel) { dialog, _ ->
                dialog.dismiss()
            }
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


    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        adjustFullScreen(newConfig, window)
    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        if (hasFocus) {
            adjustFullScreen(resources.configuration, window)
        }
    }


    override fun onUserLeaveHint() {
        super.onUserLeaveHint()
        minimize()
    }

    private fun minimize() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val d = windowManager.defaultDisplay
            enterPictureInPictureMode(pictureInPictureParamsBuilder(d)?.build()!!)
        }
    }


    override fun onPictureInPictureModeChanged(
        isInPictureInPictureMode: Boolean, configuration: Configuration?
    ) {
        super.onPictureInPictureModeChanged(isInPictureInPictureMode, configuration)
        if (isInPictureInPictureMode) {
            Timber.d("PIP mode")
            binding.bubbleTabBar.visibility = View.GONE
            binding.appBarLayout.visibility = View.GONE
            navController.navigate(R.id.pipModeFragment)
        } else {
            Timber.d("NO PIP mode")
            binding.bubbleTabBar.visibility = View.VISIBLE
            binding.appBarLayout.visibility = View.VISIBLE
            navController.navigate(R.id.homeFragment)
        }
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (Activity.RESULT_OK == resultCode) {
            when (requestCode) {
                PERMISSION_REQUEST_CODE -> {
                    restartApplication(SplashActivity::class.java)
                }

            }
        }
    }

}