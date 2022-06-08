package com.sedra.check.sample.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.Navigation
import com.sedra.check.sample.R
import com.sedra.check.sample.databinding.FragmentImagesVerificationSuccessfulBinding
import com.sedra.check.sample.view_models.SedraCheckViewModel

class ImagesVerificationSuccessfulFragment : Fragment() {
    private val binding: FragmentImagesVerificationSuccessfulBinding by lazy {
        FragmentImagesVerificationSuccessfulBinding.inflate(
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
            if (model.hasCloseJourney) {
                Navigation.findNavController(view)
                    .navigate(R.id.action_imagesVerificationSuccessfulFragment_to_customerIdFragment)
            } else {
                Navigation.findNavController(view)
                    .navigate(R.id.action_imagesVerificationSuccessfulFragment_to_kycSubissionSuccessfulFragment)
            }
        }
    }
}