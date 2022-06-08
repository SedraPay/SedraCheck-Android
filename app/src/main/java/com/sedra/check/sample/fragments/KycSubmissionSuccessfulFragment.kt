package com.sedra.check.sample.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.Navigation
import com.sedra.check.sample.R
import com.sedra.check.sample.databinding.FragmentKycSubmissionSuccessfulBinding
import com.sedra.check.sample.view_models.SedraCheckViewModel

class KycSubmissionSuccessfulFragment : Fragment() {
    private val binding: FragmentKycSubmissionSuccessfulBinding by lazy {
        FragmentKycSubmissionSuccessfulBinding.inflate(
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

        val model: SedraCheckViewModel by activityViewModels()

        binding.btnNext.setOnClickListener {
            Navigation.findNavController(view)
                .navigate(R.id.action_kycSubissionSuccessfulFragment_to_landingFragment)

            model.resetState()
        }
    }
}