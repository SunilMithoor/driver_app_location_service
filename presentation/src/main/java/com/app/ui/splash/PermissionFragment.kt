package com.app.ui.splash

import android.Manifest
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import com.app.R
import com.app.databinding.FragmentPermissionBinding
import com.app.extension.*
import com.app.ui.base.BaseFragment
import com.app.ui.base.BaseFragmentStateAdapter
import com.app.ui.onBoarding.SignInActivity
import com.app.vm.permission.PermissionVM
import com.markodevcic.peko.PermissionResult
import org.koin.androidx.viewmodel.ext.android.viewModel
import timber.log.Timber


class PermissionFragment : BaseFragment(AppLayout.fragment_permission) {

    private var _binding: FragmentPermissionBinding? = null
    private val permissionVM by viewModel<PermissionVM>()
    private val binding get() = _binding!!

    private val adapter by lazy {
        BaseFragmentStateAdapter(activityCompat)
    }

    private val fragmentList by lazy {
        val list = mutableListOf<Fragment>()
        for (i in 0..4) {
            val frag = PermissionContentFragment.newInstance(i)
            list.add(frag)
        }
        list
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityCompat.onBackPressedDispatcher.addCallback(this, object :
            OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                finish()
            }
        })
    }

    override fun onCreate(view: View) {
        binding.vpPermission.isUserInputEnabled = false
        binding.vpPermission.adapter = adapter
        adapter.submitList(fragmentList)
        binding.indicator.setViewPager(binding.vpPermission)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreateView(inflater, container, savedInstanceState)
        _binding = _binding ?: FragmentPermissionBinding.inflate(inflater, container, false)
        initialize()
        return binding.root
    }


    private fun initialize() {
    }


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnSubmit.click {
            askPermission(binding.vpPermission.currentItem)
        }
    }


    private fun askPermission(pos: Int) {
        Timber.d("Pos-->$pos")
        when (pos) {
            0 -> {
                permissionVM.checkPermissionsData(
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.READ_EXTERNAL_STORAGE
                )
            }
            1 -> {
                permissionVM.checkPermissionsData(
                    Manifest.permission.CAMERA
                )
            }
            2 -> {
                permissionVM.checkPermissionsData(
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_FINE_LOCATION
                )
            }
            3 -> {
                permissionVM.checkPermissionsData(
                    Manifest.permission.RECORD_AUDIO
                )
            }
            4 -> {
                permissionVM.checkPermissionsData(
                    Manifest.permission.READ_PHONE_STATE
                )
            }
        }
    }


    override fun observeLiveData() {
        permissionVM.permissionLiveData.observe(this, {
            setResult(it, binding.vpPermission.currentItem)
        })
    }

    private fun setResult(r: PermissionResult, pos: Int) {
        when (r) {
            is PermissionResult.Granted -> {
                Timber.d("GRANTED-->$pos")
                context?.toast(AppString.permissions_granted)
                moveNext(pos)
            }
            is PermissionResult.Denied.NeedsRationale -> {
                Timber.d("DENIED-->$pos")
                moveNext(pos)
            }
            is PermissionResult.Denied.DeniedPermanently -> {
                Timber.d("DENIED PERM-->$pos")
                showPopUp(pos)
            }
            else -> {

            }
        }
    }

    private fun moveNext(pos: Int) {
        if (pos == 4) {
            userDataManager.isPermissionGranted = true
            startActivity<SignInActivity>()
            finish()
        } else {
            binding.vpPermission.currentItem = binding.vpPermission.currentItem + 1
        }
    }

    private fun showPopUp(pos: Int) {
        var msg = ""
        when (pos) {
            0 -> {
                msg = resString(AppString.microphone_disable_perm_message)
            }
            1 -> {
                msg = resString(AppString.camera_disable_perm_message)
            }
            2 -> {
                msg = resString(AppString.storage_disable_perm_message)
            }
            3 -> {
                msg = resString(AppString.phone_disable_perm_message)
            }
            4 -> {
                msg = resString(AppString.location_disable_perm_message)
            }
        }
        context?.alert(R.style.Dialog_Alert) {
            setCancelable(false)
            setTitle(AppString.permissions_required)
            setMessage(msg)
            setPositiveButton(AppString.settings) { _, _ ->
                val intent = context.createAppSettingsIntent()
//                startActivityForResult(intent, PERMISSION_REQUEST_CODE)
                startActivity(intent)
            }
            setNegativeButton(AppString.label_cancel) { _, _ ->
                moveNext(pos)
            }
        }
    }
}