package com.sedra.check.sample.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.sedra.check.sample.R
import com.sedra.check.sample.databinding.FragmentSelectNationalityBinding

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
        binding.btnNext.setOnClickListener {
            Navigation.findNavController(view)
                .navigate(R.id.action_selectNationalityFragment_to_selectIdTypeFragment)
        }
    }
}