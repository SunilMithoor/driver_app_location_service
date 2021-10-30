package com.app.ui.sign_up

import android.os.Bundle
import com.app.databinding.ActivitySignInBinding
import com.app.databinding.ActivitySignUpBinding
import com.app.extension.viewBinding
import com.app.ui.base.BaseAppCompatActivity
import com.app.vm.signin.SignInVM
import org.koin.androidx.viewmodel.ext.android.viewModel


class SignUpActivity : BaseAppCompatActivity() {
    private val binding by viewBinding(ActivitySignUpBinding::inflate)

    private val signInVM by viewModel<SignInVM>()

    override fun layout() = binding.root

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }
}