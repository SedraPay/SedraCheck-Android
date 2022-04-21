package com.sedra.check.sample.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.Navigation
import com.sedra.check.lib.ScanResult
import com.sedra.check.lib.ScannerResultContract
import com.sedra.check.lib.enums.IdPage
import com.sedra.check.lib.enums.IdType
import com.sedra.check.lib.network.models.DataExtractionResponseModel
import com.sedra.check.sample.R
import com.sedra.check.sample.databinding.FragmentSelectIdTypeBinding
import com.sedra.check.sample.view_models.SedraCheckViewModel

class SelectIdTypeFragment : Fragment() {
    var idType:IdType? = null

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

        model.getScanDoc().observe(viewLifecycleOwner) { responseModel: DataExtractionResponseModel? ->
            if (responseModel != null) {

                Navigation.findNavController(view)
                    .navigate(R.id.action_selectIdTypeFragment_to_selfieInstructionsFragment)

                binding.btnNext.visibility = View.VISIBLE
                binding.progressIndicator.visibility = View.GONE

            }
        }

        val scans = ArrayList<ScanResult>()

        getScannerResult = registerForActivityResult(ScannerResultContract()) { imagePath ->
            if (imagePath != null) {
                binding.btnNext.visibility = View.GONE
                binding.progressIndicator.visibility = View.VISIBLE

                var idPage = IdPage.FRONT
                if(idType == IdType.ID && scans.size > 0)
                    idPage = IdPage.BACK

                scans.add(ScanResult(imagePath, idType!!, idPage))

                //do a check and call it again if you need to get the back side
                if(idType == IdType.ID && scans.size < 2) {
                    //start scanner again
                    //you can also put an intermediate screen or show a popup up dialog first, to
                    //inform your user that they will need to scan the back of their ID
                    getScannerResult.launch(IdType.ID)
                }else {
                    //else send the list of paths to the view model
                    model.extractDataFromDocuments(scans)
                }
            } else {
                // handle no result returned
            }
        }

        binding.cardNationalId.setOnClickListener {
            idType = IdType.ID
            getScannerResult.launch(IdType.ID)
        }

        binding.cardPassport.setOnClickListener {
            idType = IdType.PASSPORT
            getScannerResult.launch(IdType.PASSPORT)
        }

        binding.cardDrivingLicense.setOnClickListener {
            idType = IdType.DrivingLicense
            getScannerResult.launch(IdType.DrivingLicense)
        }
    }
}
