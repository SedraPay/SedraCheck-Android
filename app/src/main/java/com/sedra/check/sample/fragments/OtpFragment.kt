package com.sedra.check.sample.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import com.sedra.check.sample.R
import com.sedra.check.sample.databinding.FragmentOtpBinding
import com.sedra.check.sample.databinding.FragmentVerifyMobileNumberBinding

class OtpFragment : Fragment() {
    private val binding: FragmentOtpBinding by lazy {
        FragmentOtpBinding.inflate(
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

        binding.btnSubmit.setOnClickListener {
            Navigation.findNavController(view)
                .navigate(R.id.action_otpFragment_to_mobileVerificationSuccessfulFragment)
        }
    }
}