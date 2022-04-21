# SedraCheck

eKYC made simple.

`SedraCheck` is here to help you onboard your customers effortlessly.

## Requirements

Minimum SDK = 21 The target device must have Google Play Services The fowling permissions needs to
be added to your app’s manifest

```xml

<manifest xmlns:android="http://schemas.android.com/apk/res/android"
        xmlns:tools="http://schemas.android.com/tools" package="com.sedra.check.sample">

  <uses-permission android:name="android.permission.INTERNET" />
  <uses-permission android:name="android.permission.CAMERA" />
  <uses-permission android:name="android.permission.RECORD_AUDIO" />
  <uses-permission android:name="android.permission.READ_EXTERNAL_STORAGE" />
  <uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE"
          tools:ignore="ScopedStorage" />
  <uses-permission android:name="android.permission.ACCESS_MEDIA_LOCATION" />

  <!-- The rest of you manifest here -->

</manifest>
```

## Installation

The latest AAR for SedraCheck is provided within the example in this repository. You should download
and add it to your own project as
described [here](https://developer.android.com/studio/projects/android-library#psd-add-aar-jar-dependency)
.

The SDK is developed in Kotlin so you need to add the fowling at the top level of your gradle file:

```groovy
plugins {
  id 'com.android.application'
  id 'kotlin-android'
  id 'kotlin-kapt'
}
```

and make sure to have the fowling inside your ‘android’ module in gradle

```groovy
android {
  ...
  compileOptions {
    sourceCompatibility JavaVersion.VERSION_1_8
    targetCompatibility JavaVersion.VERSION_1_8
  }
  kotlinOptions {
    jvmTarget = '1.8'
  }
  ...
}
```

## Notes

> The provided sample code and all the documentation and examples bellow are all written in Kotlin.

## Initializing the SDK

To initialize the SDK you should use the Builder class provided with SedraCheckEngine to help you
setup the SDK’s functionality

### Simple initialization

```kotlin
private val sc: SedraCheck = SedraCheckEngine.Builder().subscriptionKey("YOUR_KEY_HERE").build()
```

### Full initialization

```kotlin
private val sc: SedraCheck =
  SedraCheckEngine.Builder()
    .subscriptionKey("YOUR_KEY_HERE ")
    .configureDocuemntScanner(scannedConfiguration)
    .configureLiveness(livenessConfiguration)
    .configureFaceMatch(faceMatchConfiguration)
    .build()
```

Note the SedraCheck interface provides access to all the exposed APIs of the SDK. To use any of the
functionality that will be explained in the rest of this document you need to make sure you reuse
the same instance otherwise you will lose any intermediate information between different methods. To
handle this, we recommend keeping the SedraCheck instance inside a ViewModel that will stay alive if
the user is still in the related journey. You can also keep an instance of SedraCheck class alive in
a Singleton class, or any similar way. If the instance of SedraCheck class is destroyed and
recreated, you will lose key info to connect different steps into one journey.

## Start new journey

A SedraCheck journey is comprised of a number of steps, based on your subsicription, for example
here is a full journey:

1. Start Journey
2. Scan Documents
3. Liveness check and image matching
4. Compliance check

### Dependencies

- Functions 2,3 & 4, rely on Start Journey to be able to group all the actions in those steps to one
  end user
- Function 3, relies on the Front ID image from Function 2
- Function 4 relies on the first and last name of Function 2

### Start Journey

Using the instance of SedraCheck we created in the previous section, here is how to request to start
a new journey.

Note that each time your user restarts the KYC journey you should start a new journey because each
journey will have a unique GUID to identify it.

```kotlin
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
```

The result object for this step is needed currently to tell you if you are subscribed or not

Note that all the methods use the same callback structure

```kotlin
interface CheckCallback<T> {
  fun onResult(
    result: T,
    exception: SedraCheckException? = null
  )
}
```

if the exception object is not null then the method has failed, for further information on
exceptions, read the Exceptions section in this document.

## Scan ID

The scan ID functionality is split into 2 steps

1. Document Scanner – provided as an Activity for result contract
2. Data extraction function that will take the files paths from the Document Scanner to extract data
   from the images

### Open document scanner

```kotlin
//
//In Activity or Fragment:
//
val scans = ArrayList<ScanResult>()

var getScannerResult: ActivityResultLauncher<IdType> =
  registerForActivityResult(ScannerResultContract()) { imagePath ->
    if (imagePath != null) {
      binding.btnNext.visibility = View.GONE
      binding.progressIndicator.visibility = View.VISIBLE

      scans.add(ScanResult(imagePath, IdType.ID, IdPage.FRONT))
      //do logic here if you need to get front and back image of an id you need to launch the 
      //scanner again

      //after you get all the needed images, you need to call the extractDataFromDocuments()
      //method from SedraCheck, in this example the extractDataFromDocuments() is inside a 
      //view model:
      model.extractDataFromDocuments(scans)
    } else {
      // handle no result returned
    }
  }

binding.btnNext.setOnClickListener {
  //when lauching the scanner you need to provide what type of id is being scanned from IdType enum 
  // class
  getScannerResult.launch(IdType.ID)
}
```

### Start extracting data from document images

```kotlin
//
//In ViewModel:
//
fun extractDataFromDocuments(scanResults: ArrayList<ScanResult>) {
  sc?.extractDataFromDocuments(checkCallback = object :
    CheckCallback<DataExtractionResponseModel?> {
    override fun onResult(
      result: DataExtractionResponseModel?,
      exception: SedraCheckException?
    ) {
      if (exception == null) {
        //this function will take all the images as a list of ScanResults, and will return 
        //the data extracted from the documents (if possible) in the below model
        dataExtraction.value = result ?: DataExtractionResponseModel()
      } else {
        sedraCheckException.value = exception
      }
    }
  }, scannedDocuments = scanResults)
}

//
//DataExtractionResponseModel:
//
class DataExtractionResponseModel {
  //extractedFields is a list of all the extracted data 
  var extractedFields: ArrayList<ExtractedFields> = arrayListOf()

  //validationResult represents all the validation checks performed as a list and the result of
  //each check and an overall result
  var validationResult: ValidationResult? = ValidationResult()

  //ids of images for reference
  var frontImageId: String? = null
  var backImageId: String? = null
}
```

The result object will return all the fields that SedraCheck was able to extract from the provided
images & all the validations with success/failure status for each validation

## Liveness Check and Image Matching

These 2 functions are related to each other and split into 2 steps 1- Selfie Camera – provided as an
Activity 2- Image Matching method that will compare 2 faces

### Selfie Camera

```kotlin
binding.btnTakeSelfie.setOnClickListener {
    binding.btnTakeSelfie.visibility = View.GONE
    val steps = ArrayList<FaceDetectionStep>().apply {
        //The order these steps will be displayed to the end-user will always be random,
        //except that the SMILE step will always be the last one.
        add(FaceDetectionStep.BLINK)
        add(FaceDetectionStep.LOOK_LEFT)

        //Adding the same step multiple times will be ignored, and it will be shown to the
        //end-user only once.
        add(FaceDetectionStep.LOOK_RIGHT)
        add(FaceDetectionStep.LOOK_RIGHT)

        //Note that SMILE step will always be added, even if you don't include it, and will
        //always be the last step shown to the user, this is to make sure the captured
        //selfie image is the best possible.
        add(FaceDetectionStep.SMILE)

    }

    getLivenessCheckResult.launch(steps)
}
```

### Start face matching check

```kotlin
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
```

the result object will provide you with a flag ‘isIdentical’ and confidence level (0-1)

## Screening check

Screening is used to check on your user using their full name or at least thier first and last name.

```kotlin
//in the screening request model, the first and last names are mandatory, but the second and third 
// are optional
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
```

the result object will provide you request status and request status description to show if a match
is found or not

## Close Journey

This method is used to inform the SedraCheck portal that a certain journy has been completed, and
with it you should send a customer ID.

This Customer ID, should be an ID used in your core system. This way you will have the ability to
search SedraCheck portal by customer ID.

```kotlin
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
```

## Exceptions

- All methods will return failures or errors as custom exceptions, and all exceptions inherit from
  SedraCheckException
- Each exception will have a message string to provide the developer with a clear description of the
  problem to be able to track and debug it.
- If any CheckCallback returns null exception that the method has completed its operation
  successfully. If CheckCallback returns any exception, then you can safely ignore the result
  object.
- Also CheckCallback provides rawInput object in all cases to show the developer how the SDK
  received the data you provided to further help in debug.
- SedraCheck Android SDK does not have any internal log or print statement.

## Contact Us

If you have any questions or you want to contact us, visit
our [website](https://sedracheck.sedrapay.com/) or contact us via email mob@sedrapay.com