package com.sedra.check.sample.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.Toast
import androidx.activity.addCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.Navigation
import com.sedra.check.sample.R
import com.sedra.check.sample.adapters.NationalitiesArrayAdapter
import com.sedra.check.sample.databinding.FragmentSelectNationalityBinding
import com.sedra.check.sample.view_models.SedraCheckViewModel

class SelectNationalityFragment : Fragment() {
    private val binding: FragmentSelectNationalityBinding by lazy {
        FragmentSelectNationalityBinding.inflate(
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

        model.requestNationalitiesAndIdTypes()

        model.getNationalitiesAndIdTypes().observe(viewLifecycleOwner) {
            if (it != null) {
                with(binding) {
                    progressIndicator.visibility = View.GONE
                    btnNext.visibility = View.VISIBLE
                    spinner.adapter = NationalitiesArrayAdapter(requireContext(), it.nationalities)
                }
            }
        }

        with(binding) {
            spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onNothingSelected(parent: AdapterView<*>?) {}

                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    model.setSelectedNationality(position)
                }
            }

            btnNext.setOnClickListener {
                Navigation.findNavController(view)
                    .navigate(R.id.action_selectNationalityFragment_to_selectIdTypeFragment)
            }
        }
    }
}