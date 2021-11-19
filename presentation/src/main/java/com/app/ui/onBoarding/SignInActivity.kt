package com.app.ui.onBoarding

import android.os.Bundle
import com.app.databinding.ActivitySignInBinding
import com.app.extension.viewBinding
import com.app.ui.base.BaseAppCompatActivity
import com.app.vm.onboarding.OnBoardingVM
import org.koin.androidx.viewmodel.ext.android.viewModel


class SignInActivity : BaseAppCompatActivity() {
    private val binding by viewBinding(ActivitySignInBinding::inflate)

    private val onBoardingVM by viewModel<OnBoardingVM>()

    override fun layout() = binding.root

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }
}