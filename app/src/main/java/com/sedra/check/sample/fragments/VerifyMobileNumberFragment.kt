package com.sedra.check.sample.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.sedra.check.sample.R
import com.sedra.check.sample.databinding.FragmentVerifyMobileNumberBinding

class VerifyMobileNumberFragment : Fragment() {

    private val binding: FragmentVerifyMobileNumberBinding by lazy {
        FragmentVerifyMobileNumberBinding.inflate(
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
        binding.btnVerify.setOnClickListener {
            Navigation.findNavController(view)
                .navigate(R.id.action_verifyMobileNumberFragment_to_otpFragment);
        }
    }

}