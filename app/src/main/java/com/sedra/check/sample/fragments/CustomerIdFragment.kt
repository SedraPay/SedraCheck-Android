package com.sedra.check.sample.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.Navigation
import com.sedra.check.sample.R
import com.sedra.check.sample.databinding.FragmentCustomerIdBinding
import com.sedra.check.sample.view_models.SedraCheckViewModel

class CustomerIdFragment : Fragment() {
    private val binding: FragmentCustomerIdBinding by lazy {
        FragmentCustomerIdBinding.inflate(
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
            binding.tilCustomerId.error = null
            if (binding.etCustomerId.text.toString().isEmpty()) {
                binding.tilCustomerId.error = "This field is mandatory"
            } else {
                binding.btnNext.visibility = View.GONE
                binding.progressIndicator.visibility = View.VISIBLE
                model.closeJourney(binding.etCustomerId.text.toString())
            }

        }

        model.getCloseJourney().observe(viewLifecycleOwner) {
            if (it != null) {
                binding.btnNext.visibility = View.VISIBLE
                binding.progressIndicator.visibility = View.GONE

                Navigation.findNavController(view)
                    .navigate(R.id.action_customerIdFragment_to_kycSubissionSuccessfulFragment)
            }
        }
    }
}