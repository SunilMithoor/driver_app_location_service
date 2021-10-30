package com.app.ui.splash

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.app.databinding.FragmentPermissionContentBinding
import com.app.extension.AppDrawable
import com.app.extension.AppLayout
import com.app.extension.AppString
import com.app.extension.resString
import com.app.ui.base.BaseFragment
import timber.log.Timber

class PermissionContentFragment : BaseFragment(AppLayout.fragment_permission_content) {

    private var _binding: FragmentPermissionContentBinding? = null
    private val binding get() = _binding!!

    companion object {
        fun newInstance(pos: Int): PermissionContentFragment = PermissionContentFragment().apply {
            arguments = Bundle().apply {
                putInt("pos", pos)
            }
        }
    }

    private val resIds by lazy {
        val list = mutableListOf<Triple<Int, Int, Int>>()
        list.add(
            Triple(
                AppDrawable.ic_storage_large,
                AppString.storage,
                AppString.storage_perm_message
            )
        )
        list.add(
            Triple(
                AppDrawable.ic_photo_camera_large,
                AppString.camera,
                AppString.camera_perm_message
            )
        )
        list.add(
            Triple(
                AppDrawable.ic_place_large,
                AppString.location,
                AppString.location_perm_message
            )
        )
        list.add(
            Triple(
                AppDrawable.ic_keyboard_voice_arge,
                AppString.microphone,
                AppString.microphone_perm_message
            )
        )
        list.add(
            Triple(
                AppDrawable.ic_phone_iphone_large,
                AppString.phone,
                AppString.phone_perm_message
            )
        )
        list
    }

    private val pos by lazy {
        bundle.getInt("pos", 0)
    }

    override fun onCreate(view: View) {
        setData(pos)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setData(pos)
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        _binding = _binding ?: FragmentPermissionContentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setData(pos: Int) {
        binding.ivImage.setImageResource(resIds[pos].first)
        binding.tvName.resString = resIds[pos].second
        binding.tvDescription.resString = resIds[pos].third
    }
}