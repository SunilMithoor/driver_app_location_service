package com.app.ui.splash

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import com.app.databinding.FragmentSplashBinding
import com.app.extension.AppLayout
import com.app.extension.startActivity
import com.app.ui.base.BaseFragment
import com.app.ui.dashboard.DashboardActivity
import com.app.ui.sign_in.SignInActivity
import com.app.vm.splash.SplashVM
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import timber.log.Timber


class SplashFragment : BaseFragment(AppLayout.fragment_splash) {

    private var _binding: FragmentSplashBinding? = null
    private val binding get() = _binding!!
    private val splashVM by viewModel<SplashVM>()

    override fun onCreate(view: View) {

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreateView(inflater, container, savedInstanceState)
        _binding = _binding ?: FragmentSplashBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initialize()
    }


    private fun initialize() {
        lifecycleScope.launch {
            flow {
                delay(2000)
                emit(true)
            }.collect {
                navigate()
            }
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun navigate() {
        Timber.d("perm-->${userDataManager.isPermissionGranted}")
//        navController.navigate(SplashFragmentDirections.navigateToPermissionFragment())
        when {
            userDataManager.isFirstTimeAppLaunch -> {
                navController.navigate(SplashFragmentDirections.navigateToWalkThroughFragment())
            }
            userDataManager.isUserLoggedIn -> {
                startActivity<DashboardActivity>()
                finish()
            }
            !userDataManager.isPermissionGranted -> {
                navController.navigate(SplashFragmentDirections.navigateToPermissionFragment())
            }
            else -> {
                startActivity<SignInActivity>()
                finish()
            }
        }
    }
}