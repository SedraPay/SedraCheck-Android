package com.sedra.check.sample.view_models

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.sedra.check.lib.CheckCallback
import com.sedra.check.lib.ScanResult
import com.sedra.check.lib.SedraCheck
import com.sedra.check.lib.SedraCheckEngine
import com.sedra.check.lib.exceptions.SedraCheckException
import com.sedra.check.lib.network.models.*

class SedraCheckViewModel : ViewModel() {
    private var selfieImagePath: String? = null

    //create an object of SedraCheck interface to be initialized using SedraCheckEngine.Builder below
    private var sc: SedraCheck? = null

    //these LiveData objects will be used to
    private val sedraCheckJourney: MutableLiveData<String?> =
        MutableLiveData<String?>()

    private val sedraCheckException: MutableLiveData<SedraCheckException?> =
        MutableLiveData<SedraCheckException?>()

    private val dataExtraction: MutableLiveData<DataExtractionModel> =
        MutableLiveData<DataExtractionModel>()

    private val livenessCheck: MutableLiveData<FaceMatchResponseModel> =
        MutableLiveData<FaceMatchResponseModel>()

    private val screeningCheck: MutableLiveData<ScreeningResponseModel> =
        MutableLiveData<ScreeningResponseModel>()

    private val closeJourney: MutableLiveData<CloseJourneyResponseModel> =
        MutableLiveData<CloseJourneyResponseModel>()

    fun startJourney(subscriptionKey: String, baseUrl: String, context: Context) {
        //initialize SedraCheckEngine before calling any functions, and make sure to use the same
        //object, we recommend to keep it in the ViewModel and use it across your code
        sc = sc ?: SedraCheckEngine.Builder()
            .subscriptionKey(subscriptionKey)
            .baseUrl(baseUrl)
            .build()


        //the first step must be use SedraCheckEngine to start a new journey.
        //Note that each time your user restarts the KYC journey you should start a new journey
        // because each journey will have a unique GUID to identify it.
        sc?.startJourney(
            checkCallback = object : CheckCallback<String?> {
                override fun onResult(
                    result: String?,
                    exception: SedraCheckException?
                ) {

                    //handle result and exception here

                    //if exception is not null then there is an issue and you should handle it or
                    //display a message to the user.
                    if (exception == null) {

                        //result is the journeyGuid, which should be stored on your backend to match
                        //this journey with the user ID from your backend.

                        sedraCheckJourney.value = result

                    } else {

                        //All exceptions inherit from
                        //  open class SedraCheckException (message: String) : Exception(message)
                        //notice that SedraCheckException inherits from Exception class
                        //and each exception would have a technical message explaining what happened

                        sedraCheckException.value = exception

                        //all types of errors have thier own custom exception so you can build logic
                        //to display different types of messages or ui to your user as needed.

                    }
                }
            },
            context = context
        )
    }

    fun extractDataFromDocuments(scanResults: ArrayList<ScanResult>) {
        sc?.extractDataFromDocuments(checkCallback = object :
            CheckCallback<DataExtractionModel?> {
            override fun onResult(
                result: DataExtractionModel?,
                exception: SedraCheckException?
            ) {
                if (exception == null) {
                    dataExtraction.value = result ?: DataExtractionModel()
                } else {
                    sedraCheckException.value = exception
                }
            }
        }, scannedDocuments = scanResults)
    }

    fun startSelfieMatch(selfieImagePath: String) {
        this.selfieImagePath = selfieImagePath

        //this method requires the image path extracted from the liveness check activity
        sc?.startSelfieMatch(checkCallback = object : CheckCallback<FaceMatchResponseModel?> {
            override fun onResult(
                result: FaceMatchResponseModel?,
                exception: SedraCheckException?
            ) {
                livenessCheck.value = result ?: FaceMatchResponseModel()
            }
        }, selfieImagePath = selfieImagePath)
    }

    //in the screening request model, the first and last names are mandatory, but the second and
    //third are optional
    fun checkScreening(screeningRequestModel: ScreeningRequestModel) {
        sc?.startScreening(checkCallback = object : CheckCallback<ScreeningResponseModel?> {
            override fun onResult(
                result: ScreeningResponseModel?,
                exception: SedraCheckException?
            ) {
                screeningCheck.value = result ?: ScreeningResponseModel()
            }
        }, screeningRequestModel = screeningRequestModel)
    }

    fun closeJourney(customerId: String) {
        sc?.closeJourney(checkCallback = object : CheckCallback<CloseJourneyResponseModel?> {
            override fun onResult(
                result: CloseJourneyResponseModel?,
                exception: SedraCheckException?
            ) {
                closeJourney.value = result ?: CloseJourneyResponseModel()
            }
        }, customerId = customerId)
    }

    fun getSedraCheckException(): LiveData<SedraCheckException?> {
        return sedraCheckException
    }

    fun clearException() {
        sedraCheckException.value = null
    }

    fun getSedraCheckJourney(): LiveData<String?> {
        return sedraCheckJourney
    }

    fun getScanDoc(): LiveData<DataExtractionModel> {
        return dataExtraction
    }

    fun getLivenessCheck(): LiveData<FaceMatchResponseModel> {
        return livenessCheck
    }

    fun getScreeningCheck(): LiveData<ScreeningResponseModel> {
        return screeningCheck
    }

    fun getCloseJourney(): LiveData<CloseJourneyResponseModel> {
        return closeJourney
    }


}