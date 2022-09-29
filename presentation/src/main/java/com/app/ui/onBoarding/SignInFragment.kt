package com.app.ui.onBoarding

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.app.databinding.FragmentSignInBinding
import com.app.domain.entity.FirebaseAuthResponse
import com.app.domain.entity.request.SignInRequest
import com.app.domain.extention.isEmail
import com.app.extension.*
import com.app.ui.base.BaseFragment
import com.app.ui.base.hideLoader
import com.app.ui.base.showLoader
import com.app.ui.dashboard.DashboardActivity
import com.app.vm.OnSignInUpdateEvent
import com.app.vm.onboarding.OnBoardingVM
import com.google.firebase.installations.FirebaseInstallations
import com.google.firebase.messaging.FirebaseMessaging
import org.koin.androidx.viewmodel.ext.android.viewModel
import timber.log.Timber


class SignInFragment : BaseFragment(AppLayout.fragment_sign_in) {

    private var _binding: FragmentSignInBinding? = null
    private val binding get() = _binding!!
    private val onBoardingVM by viewModel<OnBoardingVM>()
    private var deviceToken = ""
    private var messageToken = ""
    override fun onCreate(view: View) {

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreateView(inflater, container, savedInstanceState)
        _binding = _binding ?: FragmentSignInBinding.inflate(inflater, container, false)
        initialize()
        return binding.root
    }

    private fun initialize() {
//        getFireBaseDeviceToken()
//        getFireBaseMessagingToken()
        binding.btnSubmit.click {
            hideKeyboard()
            validateData()
        }

        binding.linSignUp.click {
            hideKeyboard()
            startActivity<SignUpActivity>()
        }

        binding.forgotPassword.click {
            hideKeyboard()
            navController.navigate(
                SignInFragmentDirections.navigateToForgotPasswordFragment(binding.etUsername.value)
            )
        }
    }


    private fun validate() {

        val signInRequest =
            SignInRequest(
                "",
                "",
                deviceToken
            )
        onBoardingVM.post(OnSignInUpdateEvent.UpdateSignIn(signInRequest))

    }

    private fun validateData() {
        val username = binding.etUsername.value
        val password = binding.etPassword.value
        when {
            username.isEmpty() -> {
                binding.btnSubmit.snackBar(AppString.error_email_empty)
                binding.etUsername.requestFocus()
            }
            !username.isEmail() -> {
                binding.btnSubmit.snackBar(AppString.error_invalid_email_id)
                binding.etUsername.requestFocus()
            }
            password.isEmpty() -> {
                binding.btnSubmit.snackBar(AppString.error_password_empty)
                binding.etPassword.requestFocus()
            }
            password.length < 6 -> {
                binding.btnSubmit.snackBar(AppString.error_password_length)
                binding.etPassword.requestFocus()
            }
            else -> {
                fragmentActivity?.showLoader()
                onBoardingVM.signInUser(username, password)
            }
        }
    }


//    private fun signInUser(username: String, password: String) {
//        if (fragmentActivity?.isNetworkAvailable() == true) {
//            fragmentActivity?.showLoader()
//            Firebase.auth.signInWithEmailAndPassword(username, password)
//                .addOnCompleteListener(requireActivity()) { task ->
//                    if (task.isSuccessful) {
//                        fragmentActivity?.hideLoader()
//                        userDataManager.email = task.result.user?.email
//                        userDataManager.uId = task.result.user?.uid
//                        userDataManager.isUserLoggedIn = true
//                        userDataManager.isDuty = true
//                        startActivity<DashboardActivity>()
//                        finish()
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
//            binding.btnSubmit.snackBar(AppString.error_internet)
//        }
//    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun observeLiveData() {
//        signInVM.signInLiveData.observe(viewLifecycleOwner, EventObserver(activityCompat) {
//            Timber.d("Response : $it")
//        })

        onBoardingVM.firebaseSignInResponse.observe(viewLifecycleOwner) {
            fragmentActivity?.hideLoader()
            when (it) {
                is FirebaseAuthResponse.Success -> {
                    it.data.let { user ->
                        userDataManager.email = user.email
                        userDataManager.uId = user.uid
                        userDataManager.isUserLoggedIn = true
                        userDataManager.isDuty = true
                        startActivity<DashboardActivity>()
                        finish()
                    }
                }
                is FirebaseAuthResponse.Failure -> {
                    binding.btnSubmit.snackBar(it.throwable.message)
                }
                else -> {
                    binding.btnSubmit.snackBar(AppString.error_message)
                }
            }
        }
    }

//    private fun getFireBaseDeviceToken() {
//        try {
//            if (fragmentActivity?.isNetworkAvailable() == true) {
//                FirebaseInstallations
//                    .getInstance()
//                    .getToken(false)
//                    .addOnSuccessListener {
//                        Timber.d("Firebase Device token-->${it.token}")
//                        deviceToken = it.token
//                        userDataManager.deviceToken = deviceToken
//                    }
//                    .addOnFailureListener {
//                        binding.constraintLayout.snackBar(AppString.error_firebase_id)
//                    }
//            }
//        } catch (e: Exception) {
//            e.printStackTrace()
//        }
//    }

//    private fun getFireBaseMessagingToken() {
//        try {
//            if (fragmentActivity?.isNetworkAvailable() == true) {
//                FirebaseMessaging
//                    .getInstance()
//                    .token
//                    .addOnSuccessListener { s: String ->
//                        Timber.d("Firebase Message token-->$s")
//                        messageToken = s
//                        userDataManager.fireBaseToken = s
//                    }
//                    .addOnFailureListener {
//                        binding.constraintLayout.snackBar(AppString.error_firebase_token)
//                    }
//            }
//        } catch (e: Exception) {
//            e.printStackTrace()
//        }
//    }
}