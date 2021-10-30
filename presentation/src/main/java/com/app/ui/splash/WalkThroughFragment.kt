package com.app.ui.splash

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2.OnPageChangeCallback
import com.app.databinding.FragmentWalkThroughBinding
import com.app.extension.*
import com.app.ui.base.BaseFragment
import com.app.ui.base.BaseFragmentStateAdapter
import com.app.ui.dashboard.DashboardActivity
import com.app.ui.sign_in.SignInActivity
import timber.log.Timber


class WalkThroughFragment : BaseFragment(AppLayout.fragment_walk_through) {

    private var _binding: FragmentWalkThroughBinding? = null
    private val binding get() = _binding!!

    private val adapter by lazy {
        BaseFragmentStateAdapter(activityCompat)
    }

    private val fragmentList by lazy {
        val list = mutableListOf<Fragment>()
        for (i in 0..3) {
            val frag = WalkThroughContentFragment.newInstance(i)
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
        binding.vpWalkThrough.adapter = adapter
        adapter.submitList(fragmentList)
        binding.indicator.setViewPager(binding.vpWalkThrough)
        binding.vpWalkThrough.registerOnPageChangeCallback(object : OnPageChangeCallback() {
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels)
            }

            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                if (position == 3) {
                    binding.btnSkip.visibility = GONE
                    binding.btnNext.text = getString(AppString.label_finish)
                } else {
                    binding.btnSkip.visibility = VISIBLE
                    binding.btnNext.text = getString(AppString.label_next)
                }
            }

            override fun onPageScrollStateChanged(state: Int) {
                super.onPageScrollStateChanged(state)
            }
        })
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        _binding = _binding ?: FragmentWalkThroughBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnSkip.click {
            navigate()
        }

        binding.btnNext.click {
            if (binding.vpWalkThrough.currentItem == 3) {
                navigate()
            } else {
                binding.vpWalkThrough.currentItem = binding.vpWalkThrough.currentItem + 1
            }
        }
    }

    private fun navigate() {
        userDataManager.isFirstTimeAppLaunch = false
        Timber.d("perm-->${userDataManager.isPermissionGranted}")
        if (userDataManager.isUserLoggedIn) {
            startActivity<DashboardActivity>()
            finish()
        } else {
            if (userDataManager.isPermissionGranted) {
                startActivity<SignInActivity>()
                finish()
            } else {
                navController.navigate(WalkThroughFragmentDirections.navigateToPermissionFragment())
            }
        }
    }
}