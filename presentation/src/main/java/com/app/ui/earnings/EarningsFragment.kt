package com.app.ui.earnings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.app.databinding.FragmentDashboardBinding
import com.app.databinding.FragmentEarningsBinding
import com.app.extension.AppLayout
import com.app.ui.base.BaseFragment
import com.app.vm.dashboard.DashboardVM
import org.koin.androidx.viewmodel.ext.android.viewModel


class EarningsFragment : BaseFragment(AppLayout.fragment_earnings) {

    private var _binding: FragmentEarningsBinding? = null
    private val binding get() = _binding!!
    private val dashboardVM by viewModel<DashboardVM>()

    override fun onCreate(view: View) {

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreateView(inflater, container, savedInstanceState)
        _binding = _binding ?: FragmentEarningsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun observeLiveData() {

    }
}