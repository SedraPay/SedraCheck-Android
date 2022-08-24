package com.sedra.check.sample.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import androidx.activity.result.ActivityResultLauncher
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.Navigation
import com.sedra.check.lib.ScanResult
import com.sedra.check.lib.ScannerResultContract
import com.sedra.check.lib.network.models.DataExtractionModel
import com.sedra.check.lib.network.models.IdType
import com.sedra.check.sample.R
import com.sedra.check.sample.adapters.IdTypesArrayAdapter
import com.sedra.check.sample.adapters.NationalitiesArrayAdapter
import com.sedra.check.sample.databinding.FragmentSelectIdTypeBinding
import com.sedra.check.sample.view_models.SedraCheckViewModel

class SelectIdTypeFragment : Fragment() {
    var idType: IdType? = null

    private val binding: FragmentSelectIdTypeBinding by lazy {
        FragmentSelectIdTypeBinding.inflate(
            layoutInflater
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        return binding.root
    }

    lateinit var getScannerResult: ActivityResultLauncher<IdType>

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val model: SedraCheckViewModel by activityViewModels()

        binding.spinner.adapter = IdTypesArrayAdapter(requireContext(), model.getIdTypes()!!)

        model.getScanDoc().observe(viewLifecycleOwner) { scanModel: DataExtractionModel? ->
            if (scanModel != null) {

                Navigation.findNavController(view)
                    .navigate(R.id.action_selectIdTypeFragment_to_kycFormFragment)

                binding.btnNext.visibility = View.VISIBLE
                binding.progressIndicator.visibility = View.GONE


            }
        }

        val scans = ArrayList<ScanResult>()

        getScannerResult = registerForActivityResult(ScannerResultContract()) { imagePath ->
            if (imagePath != null) {
                binding.btnNext.visibility = View.GONE
                binding.progressIndicator.visibility = View.VISIBLE

                scans.add(ScanResult(imagePath, idType!!))

                //do a check and call it again if you need to get the back side
                if(scans.size < (idType?.numberOfImages ?: 1)) {
                    //start scanner again
                    //you can also put an intermediate screen or show a popup up dialog first, to
                    //inform your user that they will need to scan the back of their ID
                    getScannerResult.launch(idType)
                }else {
                    //else send the list of paths to the view model
                    model.extractDataFromDocuments(scans)
                }
            } else {
                // handle no result returned
            }
        }

        with(binding){
            spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onNothingSelected(parent: AdapterView<*>?) {}

                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    idType = model.getIdTypes()!![position]
                }
            }

            btnNext.setOnClickListener {
                getScannerResult.launch(idType)
            }
        }
    }
}
