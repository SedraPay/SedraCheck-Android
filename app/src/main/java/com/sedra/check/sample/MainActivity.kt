package com.sedra.check.sample

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.Navigation.findNavController
import com.sedra.check.lib.SedraCheck
import com.sedra.check.sample.databinding.ActivityMainBinding
import com.sedra.check.sample.view_models.SedraCheckViewModel

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var sc: SedraCheck

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        val model: SedraCheckViewModel by viewModels()
        model.getSedraCheckException()?.observe(this) {
            if (it != null) {
                val builder = AlertDialog.Builder(this)
                builder.setMessage(it.localizedMessage)
                builder.setPositiveButton("OK") { dialogInterface, _ ->
                    dialogInterface.dismiss()
                    model.clearException()
                }

                // Create the AlertDialog
                val alertDialog: AlertDialog = builder.create()
                // Set other dialog properties
                alertDialog.setCancelable(false)
                alertDialog.show()
            }


        }
    }

    override fun onSupportNavigateUp() =
        findNavController(this, binding.navHostFragment.id).navigateUp()

}