package com.app.ui.splash

import android.os.Bundle
import com.app.databinding.ActivitySplashBinding
import com.app.extension.viewBinding
import com.app.ui.base.BaseAppCompatActivity
import com.app.vm.splash.SplashVM
import org.koin.androidx.viewmodel.ext.android.viewModel


class SplashActivity : BaseAppCompatActivity() {
    private val binding by viewBinding(ActivitySplashBinding::inflate)

    private val splashVM by viewModel<SplashVM>()

    override fun layout() = binding.root

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }
}