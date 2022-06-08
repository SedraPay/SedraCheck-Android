package com.sedra.check.sample.fragments

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.Navigation
import com.sedra.check.sample.R
import com.sedra.check.sample.databinding.FragmentLandingBinding
import com.sedra.check.sample.view_models.SedraCheckViewModel

class LandingFragment : Fragment() {

    private val binding: FragmentLandingBinding by lazy {
        FragmentLandingBinding.inflate(
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

        val sharedPref = activity?.getPreferences(Context.MODE_PRIVATE)

        val model: SedraCheckViewModel by activityViewModels()

        model.getSedraCheckJourney().observe(viewLifecycleOwner) {
            binding.progressIndicator.visibility = View.GONE

            if (it != null && it.isNotEmpty()) {

                //The services of SedraCheck are flexable and usually don't rely on each other that
                // much, this is why the landing page has a few Switches to allow you to select
                // which features you want to include in the current journey.
                //This will control the navigation path.
                if (model.hasDocumentScanner) {
                    Navigation.findNavController(view)
                        .navigate(R.id.action_landingFragment_to_selectNationalityFragment)
                } else if (model.hasScreening) {
                    Navigation.findNavController(view)
                        .navigate(R.id.action_landingFragment_to_NameForScreeningFragment)
                } else if (model.hasLivenessCheck) {
                    Navigation.findNavController(view)
                        .navigate(R.id.action_landingFragment_to_selfieInstructionsFragment)
                } else if (model.hasCloseJourney) {
                    Navigation.findNavController(view)
                        .navigate(R.id.action_landingFragment_to_customerIdFragment)
                }else{
                    Navigation.findNavController(view)
                        .navigate(R.id.action_landingFragment_to_kycSubissionSuccessfulFragment)
                }

            }

            binding.btnCreateAccount.visibility = View.VISIBLE
        }

        withBinding(sharedPref, model)


    }

    private fun withBinding(
        sharedPref: SharedPreferences?,
        model: SedraCheckViewModel
    ) {
        with(binding) {
            etKey.setText(sharedPref!!.getString("SC_SUB_KEY", ""))

            swDocumentScanner.setOnCheckedChangeListener { _, isChecked ->
                model.hasDocumentScanner = isChecked
            }

            swScreening.setOnCheckedChangeListener { _, isChecked ->
                model.hasScreening = isChecked
            }

            swLivenessCheck.setOnCheckedChangeListener { _, isChecked ->
                model.hasLivenessCheck = isChecked
            }

            swCloseJourney.setOnCheckedChangeListener { _, isChecked ->
                model.hasCloseJourney = isChecked
            }

            btnCreateAccount.setOnClickListener {
                binding.btnCreateAccount.visibility = View.GONE
                binding.progressIndicator.visibility = View.VISIBLE

                val editor = sharedPref.edit()
                editor.putString("SC_SUB_KEY", binding.etKey.text.toString())
                editor.apply()

                model.startJourney(
                    binding.etKey.text.toString(),
                    requireContext().getString(R.string.base_url),
                    requireActivity()
                )
            }
        }
    }

    override fun onResume() {
        super.onResume()
        binding.btnCreateAccount.visibility = View.VISIBLE
    }

}