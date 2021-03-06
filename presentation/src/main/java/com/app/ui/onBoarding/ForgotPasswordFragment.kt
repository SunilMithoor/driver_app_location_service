package com.app.ui.onBoarding

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.navigation.fragment.navArgs
import com.app.R
import com.app.databinding.FragmentForgotPasswordBinding
import com.app.domain.entity.FirebaseAuthResponse
import com.app.domain.extention.isEmail
import com.app.extension.*
import com.app.ui.base.BaseFragment
import com.app.ui.base.hideLoader
import com.app.ui.base.showLoader
import com.app.vm.onboarding.OnBoardingVM
import org.koin.androidx.viewmodel.ext.android.viewModel


class ForgotPasswordFragment : BaseFragment(AppLayout.fragment_forgot_password) {

    private var _binding: FragmentForgotPasswordBinding? = null
    private val binding get() = _binding!!
    private val args by navArgs<ForgotPasswordFragmentArgs>()
    private val onBoardingVM by viewModel<OnBoardingVM>()

    override fun onCreate(view: View) {
        val callback = requireActivity().onBackPressedDispatcher.addCallback(this) {
            onSupportNavigateUp()
        }
        callback.isEnabled = true
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreateView(inflater, container, savedInstanceState)
        _binding = _binding ?: FragmentForgotPasswordBinding.inflate(inflater, container, false)
        initialize()
        return binding.root
    }

    private fun initialize() {

        binding.btnSubmit.click {
            hideKeyboard()
            validateData()
        }
        binding.etUsername.setText(args.data)
    }


    private fun validateData() {
        val username = binding.etUsername.value
        when {
            username.isEmpty() -> {
                binding.btnSubmit.snackBar(R.string.error_email_empty)
                binding.etUsername.requestFocus()
            }
            !username.isEmail() -> {
                binding.btnSubmit.snackBar(R.string.error_invalid_email_id)
                binding.etUsername.requestFocus()
            }
            else -> {
//                resetPassword(username)
                fragmentActivity?.showLoader()
                onBoardingVM.resetPassword(username)
            }
        }
    }

    override fun observeLiveData() {
        onBoardingVM.firebaseResetPasswordResponse.observe(viewLifecycleOwner, {
            fragmentActivity?.hideLoader()
            when (it) {
                is FirebaseAuthResponse.Success -> {
                    it.data.let {
                        binding.btnSubmit.longSnackBar(AppString.reset_password_sent_message)
                        onSupportNavigateUp()
                    }
                }
                is FirebaseAuthResponse.Failure -> {
                    binding.btnSubmit.snackBar(it.throwable.message)
                }
                else -> {
                    binding.btnSubmit.snackBar(AppString.error_message)
                }
            }
        })
    }


//    private fun resetPassword(username: String) {
//        if (fragmentActivity?.isNetworkAvailable() == true) {
//            fragmentActivity?.showLoader()
//            Firebase.auth.sendPasswordResetEmail(username)
//                .addOnCompleteListener(requireActivity()) { task ->
//                    if (task.isSuccessful) {
//                        fragmentActivity?.hideLoader()
//                        binding.btnSubmit.longSnackBar(AppString.reset_password_sent_message)
//                        onSupportNavigateUp()
//                    } else {
//                        fragmentActivity?.hideLoader()
//                        binding.btnSubmit.snackBar(task.exception?.message)
//                    }
//                }
//                .addOnCanceledListener {
//                    fragmentActivity?.hideLoader()
//                }
//                .addOnFailureListener {
//                    fragmentActivity?.hideLoader()
//                }
//        } else {
//            binding.btnSubmit.snackBar(R.string.error_internet)
//        }
//    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}