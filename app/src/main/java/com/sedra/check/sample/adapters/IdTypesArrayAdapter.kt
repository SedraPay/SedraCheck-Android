package com.sedra.check.sample.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.sedra.check.lib.network.models.IdType
import com.sedra.check.lib.network.models.Nationality
import com.sedra.check.sample.R

class IdTypesArrayAdapter(context: Context, var items: ArrayList<IdType>) :
    ArrayAdapter<IdType>(context, R.layout.spinner_item, items) {

    val inflater: LayoutInflater = LayoutInflater.from(context)

    override fun getItemId(position: Int): Long {
        return items[position].typeId?.toLong() ?: -1
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var view: View? = convertView
        if (view == null) {
            view = inflater.inflate(R.layout.spinner_item, parent, false)
        }
        (view?.findViewById(android.R.id.text1) as TextView).text = "${getItem(position)!!.name} (${getItem(position)!!.numberOfImages} pages)"
        return view
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        var view: View? = convertView
        if (view == null) {
            view = inflater.inflate(R.layout.spinner_item, parent, false)
        }
        (view?.findViewById(android.R.id.text1) as TextView).text = "${getItem(position)!!.name} (${getItem(position)!!.numberOfImages} pages)"
        return view
    }
}