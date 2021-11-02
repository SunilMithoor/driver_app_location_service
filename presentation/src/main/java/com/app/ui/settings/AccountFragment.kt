package com.app.ui.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.app.R
import com.app.databinding.FragmentAccountBinding
import com.app.extension.*
import com.app.utilities.Logout
import com.app.utilities.Notification
import com.app.ui.settings.adapter.AccountsAdapter
import com.app.ui.base.BaseFragment
import com.app.ui.sign_in.SignInActivity


class AccountFragment : BaseFragment(AppLayout.fragment_account) {

    private var _binding: FragmentAccountBinding? = null
    private val binding get() = _binding!!

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
        adapter.submitList(list)
        adapter.click { _, navigationDrawer ->
            when (navigationDrawer) {
                is Notification -> {

                }
                is Logout -> {
                    userDataManager.logOut()
                    startActivity<SignInActivity>()
                    requireActivity().finishAffinity()
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}