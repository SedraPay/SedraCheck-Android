package com.sedra.check.sample.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
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

        binding.etKey.setText(sharedPref!!.getString("SC_SUB_KEY", ""))

        val model: SedraCheckViewModel by activityViewModels()

        model.getSedraCheckJourney().observe(viewLifecycleOwner) {
            binding.progressIndicator.visibility = View.GONE
            if (it != null && it.isNotEmpty()) {
                Navigation.findNavController(view)
                    .navigate(R.id.action_landingFragment_to_verifyMobileNumberFragment)
            } else {
                binding.btnCreateAccount.visibility = View.VISIBLE
            }
        }

        binding.btnCreateAccount.setOnClickListener {
            binding.btnCreateAccount.visibility = View.GONE
            binding.progressIndicator.visibility = View.VISIBLE

            val editor = sharedPref.edit()
            editor.putString("SC_SUB_KEY", binding.etKey.text.toString())
            editor.apply()

            model.startJourney(
                binding.etKey.text.toString(),
                requireContext().getString(R.string.base_url),
                requireContext()
            )
        }
    }

    override fun onResume() {
        super.onResume()
        binding.btnCreateAccount.visibility = View.VISIBLE
    }

}