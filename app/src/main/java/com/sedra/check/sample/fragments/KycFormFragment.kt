package com.sedra.check.sample.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.Navigation
import com.sedra.check.lib.network.models.ScreeningRequestModel
import com.sedra.check.sample.R
import com.sedra.check.sample.databinding.FragmentKycFormBinding
import com.sedra.check.sample.view_models.SedraCheckViewModel

class KycFormFragment : Fragment() {
    private val binding: FragmentKycFormBinding by lazy {
        FragmentKycFormBinding.inflate(
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
            etFullEnglishName.setText(
                model.getScanDoc().value?.extractedFields?.find { it.name == "Full Name" }?.value
                    ?: ""
            )

            etFullArabicName.setText(
                model.getScanDoc().value?.extractedFields?.find { it.name == "Full Name Native" }?.value
                    ?: ""
            )

            etGender.setText(
                model.getScanDoc().value?.extractedFields?.find { it.name == "Sex" }?.value
                    ?: ""
            )

            etDateOfBirth.setText(
                model.getScanDoc().value?.extractedFields?.find { it.name == "Birth Date" }?.value
                    ?: ""
            )

            etDocumentIdNumber.setText(
                model.getScanDoc().value?.extractedFields?.find { it.name == "Document Number" }?.value
                    ?: ""
            )

            etNationalIdNumber.setText(
                model.getScanDoc().value?.extractedFields?.find { it.name == "Personal Number" }?.value
                    ?: ""
            )

            etIdExpiry.setText(
                model.getScanDoc().value?.extractedFields?.find { it.name == "Expiry Date" }?.value
                    ?: ""
            )

            etAdress.setText(
                model.getScanDoc().value?.extractedFields?.find { it.name == "Address" }?.value
                    ?: ""
            )

            btnSubmit.setOnClickListener {

                var isValid = true

                if (binding.etFullEnglishName.text.toString().isEmpty()) {
                    isValid = false
                    binding.tilFullEnglishName.error = "Full name can't be empty"
                } else if (binding.etFullEnglishName.text.toString().split(' ').size < 2) {
                    isValid = false
                    binding.tilFullEnglishName.error = "Full name must be 2 parts at least"
                }

                if (isValid) {
                    binding.tilFullEnglishName.error = null
                    binding.btnSubmit.visibility = View.GONE
                    binding.progressIndicator.visibility = View.VISIBLE
                    val screeningRequestModel = ScreeningRequestModel()
                    val namePart = binding.etFullEnglishName.text.toString().split(' ')
                    screeningRequestModel.firstName = namePart.first()
                    screeningRequestModel.lastName = namePart.last()
                    model.checkScreening(screeningRequestModel)
                }
            }
        }

        with(model) {
            getScreeningCheck().observe(viewLifecycleOwner) {
                Navigation.findNavController(view)
                    .navigate(R.id.action_kycFormFragment_to_customerIdFragment)

                binding.btnSubmit.visibility = View.VISIBLE
                binding.progressIndicator.visibility = View.GONE

            }
        }


    }
}