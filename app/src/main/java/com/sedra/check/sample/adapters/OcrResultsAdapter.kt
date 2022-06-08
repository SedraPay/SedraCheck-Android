package com.sedra.check.sample.adapters

import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.sedra.check.lib.network.models.ExtractedFields
import com.sedra.check.lib.network.models.ValidationChecks
import com.sedra.check.sample.R
import com.sedra.check.sample.inflate

class OcrResultsAdapter(
    var extractedFields: ArrayList<ExtractedFields>,
    var validationChecks: ArrayList<ValidationChecks>,
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            1 -> FieldHolder(parent.inflate(R.layout.item_ocr_result, false))
            else -> ValidationHolder(parent.inflate(R.layout.item_ocr_validation, false))
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (position < extractedFields.size)
            (holder as FieldHolder).bindResult(extractedFields[position])
        else
            (holder as ValidationHolder).bindResult(validationChecks[position - extractedFields.size])

    }

    override fun getItemViewType(position: Int): Int {
        return if (position < extractedFields.size) 1 else 2
    }

    override fun getItemCount() = extractedFields.size + validationChecks.size

    class FieldHolder(v: View) : RecyclerView.ViewHolder(v) {
        private var view: View = v

        fun bindResult(field: ExtractedFields) {
            view.findViewById<TextView>(R.id.extractedFieldName).text = field.name
            view.findViewById<TextView>(R.id.extractedFieldValue).text = field.value
        }

    }

    class ValidationHolder(v: View) : RecyclerView.ViewHolder(v) {
        private var view: View = v

        fun bindResult(field: ValidationChecks) {
            view.findViewById<TextView>(R.id.validationName).text = field.name
            view.findViewById<TextView>(R.id.validationValue).text = field.value
            view.findViewById<TextView>(R.id.validationResult).text = field.result
        }
    }
}