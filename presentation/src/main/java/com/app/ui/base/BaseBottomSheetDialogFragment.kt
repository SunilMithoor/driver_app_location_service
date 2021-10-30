package com.app.ui.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.app.domain.manager.UserPrefDataManager
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import org.koin.android.ext.android.inject

abstract class BaseBottomSheetDialogFragment : BottomSheetDialogFragment() {
    val navController by lazy {
        findNavController()
    }
    val userDataManager by inject<UserPrefDataManager>()
    open fun observeLiveData() {}
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        observeLiveData()
        return super.onCreateView(inflater, container, savedInstanceState)
    }
}