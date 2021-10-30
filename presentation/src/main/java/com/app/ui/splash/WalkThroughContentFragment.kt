package com.app.ui.splash

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.app.databinding.FragmentWalkThroughContentBinding
import com.app.extension.AppLayout
import com.app.extension.AppRaw
import com.app.extension.AppString
import com.app.extension.resString
import com.app.ui.base.BaseFragment

class WalkThroughContentFragment : BaseFragment(AppLayout.fragment_walk_through_content) {

    private var _binding: FragmentWalkThroughContentBinding? = null
    private val binding get() = _binding!!

    companion object {
        fun newInstance(pos: Int): WalkThroughContentFragment = WalkThroughContentFragment().apply {
            arguments = Bundle().apply {
                putInt("pos", pos)
            }
        }
    }

    private val resIds by lazy {
        val list = mutableListOf<Triple<Int, Int, Int>>()
        list.add(
            Triple(
                AppString.label_page_1_title,
                AppString.label_page_1_msg,
                AppRaw.signin_animation
            )
        )
        list.add(
            Triple(
                AppString.label_page_2_title,
                AppString.label_page_2_msg,
                AppRaw.forklift_loading_truck
            )
        )
        list.add(
            Triple(
                AppString.label_page_3_title,
                AppString.label_page_3_msg,
                AppRaw.area_map
            )
        )
        list.add(
            Triple(
                AppString.label_page_4_title,
                AppString.label_page_4_msg,
                AppRaw.money
            )
        )
        list
    }


    private val pos by lazy {
        bundle.getInt("pos", 0)
    }


    override fun onCreate(view: View) {
        binding.tvName.resString = resIds[pos].first
        binding.tvMessage.resString = resIds[pos].second
        binding.ivImage.setAnimation(resIds[pos].third)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        _binding = _binding ?: FragmentWalkThroughContentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    fun setData(pos: Int) {
        binding.tvName.resString = resIds[pos].first
        binding.tvMessage.resString = resIds[pos].second
        binding.ivImage.setAnimation(resIds[pos].third)
    }
}