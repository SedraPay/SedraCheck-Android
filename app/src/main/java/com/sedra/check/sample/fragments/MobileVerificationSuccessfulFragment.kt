package com.sedra.check.sample.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import com.sedra.check.sample.R
import com.sedra.check.sample.databinding.FragmentLandingBinding
import com.sedra.check.sample.databinding.FragmentMobileVerificationSuccessfulBinding

class MobileVerificationSuccessfulFragment : Fragment() {
    private val binding: FragmentMobileVerificationSuccessfulBinding by lazy {
        FragmentMobileVerificationSuccessfulBinding.inflate(
            layoutInflater
        )
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnNext.setOnClickListener {
            Navigation.findNavController(view)
                .navigate(R.id.action_mobileVerificationSuccessfulFragment_to_selectNationalityFragment)
        }
    }
}