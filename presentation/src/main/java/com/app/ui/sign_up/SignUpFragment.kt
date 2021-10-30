package com.app.ui.sign_up

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.app.R
import com.app.databinding.FragmentSignUpBinding
import com.app.domain.extention.isEmail
import com.app.extension.*
import com.app.ui.base.BaseFragment
import com.app.ui.base.hideLoader
import com.app.ui.base.showLoader
import com.app.vm.signin.SignInVM
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import org.koin.androidx.viewmodel.ext.android.viewModel
import timber.log.Timber


class SignUpFragment : BaseFragment(AppLayout.fragment_sign_up) {

    private var _binding: FragmentSignUpBinding? = null
    private val binding get() = _binding!!
    private val signInVM by viewModel<SignInVM>()

    override fun onCreate(view: View) {

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreateView(inflater, container, savedInstanceState)
        _binding = _binding ?: FragmentSignUpBinding.inflate(inflater, container, false)
        initialize()
        return binding.root
    }

    private fun initialize() {
        binding.btnSubmit.click {
            hideKeyboard()
            validateData()
        }
        binding.linSignIn.click {
            finish()
        }
    }


    private fun validateData() {
        val username = binding.etUsername.value
        val password = binding.etPassword.value
        val confirmPassword = binding.etConfirmPassword.value
        when {
            username.isEmpty() -> {
                binding.btnSubmit.snackBar(R.string.error_email_empty)
                binding.etUsername.requestFocus()
            }
            !username.isEmail() -> {
                binding.btnSubmit.snackBar(R.string.error_invalid_email_id)
                binding.etUsername.requestFocus()
            }
            password.isEmpty() -> {
                binding.btnSubmit.snackBar(R.string.error_password_empty)
                binding.etPassword.requestFocus()
            }
            password.length < 6 -> {
                binding.btnSubmit.snackBar(R.string.error_password_length)
                binding.etPassword.requestFocus()
            }
            confirmPassword != password -> {
                binding.btnSubmit.snackBar(R.string.error_password_not_match)
                binding.etConfirmPassword.requestFocus()
            }
            else -> {
                registerUser(username, password)
            }
        }
    }

    private fun registerUser(username: String, password: String) {
        if (fragmentActivity?.isNetworkAvailable() == true) {
            fragmentActivity?.showLoader()
            Firebase.auth.createUserWithEmailAndPassword(username, password)
                .addOnCompleteListener(requireActivity()) { task ->
                    if (task.isSuccessful) {
                        binding.btnSubmit.snackBar(R.string.user_register_success)
                        fragmentActivity?.hideLoader()
                        finish()
                    } else {
                        fragmentActivity?.hideLoader()
                        binding.btnSubmit.snackBar(task.exception?.message)
                    }
                }
                .addOnCanceledListener {
                    fragmentActivity?.hideLoader()
                }
                .addOnFailureListener {
                    fragmentActivity?.hideLoader()
                }
        } else {
            binding.btnSubmit.snackBar(R.string.error_internet)
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun observeLiveData() {
        signInVM.signInLiveData.observe(viewLifecycleOwner, EventObserver(activityCompat) {
            Timber.d("Response : $it")
        })
    }


}