package com.app.ui.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.recyclerview.widget.LinearLayoutManager
import com.app.R
import com.app.databinding.FragmentAccountBinding
import com.app.domain.entity.FirebaseAuthResponse
import com.app.extension.*
import com.app.services.locations.LocationService
import com.app.ui.base.BaseFragment
import com.app.ui.base.hideLoader
import com.app.ui.onBoarding.SignInActivity
import com.app.ui.settings.adapter.AccountsAdapter
import com.app.utilities.Logout
import com.app.utilities.Notification
import com.app.vm.onboarding.OnBoardingVM
import org.koin.androidx.viewmodel.ext.android.viewModel
import timber.log.Timber


class AccountFragment : BaseFragment(AppLayout.fragment_account) {

    private var _binding: FragmentAccountBinding? = null
    private val binding get() = _binding!!
    private val onBoardingVM by viewModel<OnBoardingVM>()


    private val adapter by lazy {
        AccountsAdapter()
    }


    private val list = listOf(
        Notification(R.drawable.ic_notifications, R.string.notification),
        Logout(R.drawable.ic_logout, R.string.sign_out)
    )


    override fun onCreate(view: View) {
        activityCompat.showSupportActionBar()
        activityCompat.setSupportActionBarTitle(AppString.account)
        val callback = requireActivity().onBackPressedDispatcher.addCallback(this) {
            onSupportNavigateUp()
        }
        callback.isEnabled = true
        initRecyclerView()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreateView(inflater, container, savedInstanceState)
        _binding = _binding ?: FragmentAccountBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setData()
    }


    private fun initRecyclerView() {
        binding.recyclerView.layoutManager =
            LinearLayoutManager(requireActivity(), LinearLayoutManager.VERTICAL, false)
        binding.recyclerView.adapter = adapter
    }


    private fun setData() {
        onBoardingVM.getUser()
        adapter.submitList(list)
        adapter.click { _, navigationDrawer ->
            when (navigationDrawer) {
                is Notification -> {

                }
                is Logout -> {
                    onBoardingVM.signOut()
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


    override fun observeLiveData() {
        onBoardingVM.firebaseSignOutResponse.observe(viewLifecycleOwner, {
            fragmentActivity?.hideLoader()
            when (it) {
                is FirebaseAuthResponse.Success -> {
                    it.data.let { user ->
                        Timber.d("signout message-->${user.signOut}")
                        if (user.signOut == true) {
                            stopService()
                            userDataManager.logOut()
                            startActivity<SignInActivity>()
                            requireActivity().finishAffinity()
                        }
                    }
                }
                is FirebaseAuthResponse.Failure -> {
                    binding.constraintLayout.snackBar(it.throwable.message)
                }
                else -> {
                    binding.constraintLayout.snackBar(AppString.error_message)
                }
            }
        })

        onBoardingVM.firebaseGetUserResponse.observe(viewLifecycleOwner, {
            fragmentActivity?.hideLoader()
            when (it) {
                is FirebaseAuthResponse.Success -> {
                    it.data.let { user ->
                        Timber.d("get user details-->${user.email}")
                    }
                }
                is FirebaseAuthResponse.Failure -> {
                    binding.constraintLayout.snackBar(it.throwable.message)
                }
                else -> {
                    binding.constraintLayout.snackBar(AppString.error_message)
                }
            }
        })
    }

    private fun stopService() {
        context?.stopService(LocationService::class.java)
    }
}