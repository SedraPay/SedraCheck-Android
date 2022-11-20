package com.sedra.check.sample.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.sedra.check.lib.network.models.DataExtractionModel
import com.sedra.check.sample.R
import com.sedra.check.sample.adapters.OcrResultsAdapter
import com.sedra.check.sample.databinding.FragmentOcrResultsBinding
import com.sedra.check.sample.view_models.SedraCheckViewModel

class OcrResultsFragment : Fragment() {
    private lateinit var linearLayoutManager: LinearLayoutManager
    private lateinit var adapter: OcrResultsAdapter

    private val binding: FragmentOcrResultsBinding by lazy {
        FragmentOcrResultsBinding.inflate(
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

        with(binding) {
            linearLayoutManager = LinearLayoutManager(requireContext())
            recyclerView.layoutManager = linearLayoutManager

            adapter = OcrResultsAdapter(
                model.getScanDoc().value?.extractedFields ?: arrayListOf(),
                model.getScanDoc().value?.validationResult?.validationChecks ?: arrayListOf()
            )

            recyclerView.adapter = adapter

            btnSubmit.setOnClickListener {

                //The services of SedraCheck are flexable and usually don't rely on each other that
                // much, this is why the landing page has a few Switches to allow you to select
                // which features you want to include in the current journey.
                //This will control the navigation path.
                if (model.hasScreening) {
                    Navigation.findNavController(view)
                        .navigate(R.id.action_kycFormFragment_to_NameForScreeningFragment)
                } else if (model.hasLivenessCheck) {
                    Navigation.findNavController(view)
                        .navigate(R.id.action_kycFormFragment_to_selfieInstructionsFragment)
                } else if (model.hasCloseJourney) {
                    Navigation.findNavController(view)
                        .navigate(R.id.action_kycFormFragment_to_customerIdFragment)
                } else {
                    Navigation.findNavController(view)
                        .navigate(R.id.action_kycFormFragment_to_kycSubissionSuccessfulFragment)
                }
            }
            tvResults.text =
                "result: ${model.getScanDoc().value?.validationResult?.result} | checks: ${model.getScanDoc().value?.validationResult?.successfulCount}/${model.getScanDoc().value?.validationResult?.successfulCount}"
        }
    }
}