package com.sedra.check.sample.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.sedra.check.lib.network.models.Nationality
import com.sedra.check.sample.R

class NationalitiesArrayAdapter(context: Context, var items: List<Nationality>) :
    ArrayAdapter<Nationality>(context, R.layout.spinner_item, items) {

    val inflater: LayoutInflater = LayoutInflater.from(context)

    override fun getItemId(position: Int): Long {
        return items[position].nationalityId?.toLong() ?: -1
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var view: View? = convertView
        if (view == null) {
            view = inflater.inflate(R.layout.spinner_item, parent, false)
        }
        (view?.findViewById(R.id.text) as TextView).text = getItem(position)!!.name
        return view
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        var view: View? = convertView
        if (view == null) {
            view = inflater.inflate(R.layout.spinner_item, parent, false)
        }
        (view?.findViewById(R.id.text) as TextView).text = getItem(position)!!.name
        return view
    }
}