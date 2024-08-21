package com.am.projectinternalresto.ui.adapter.manage_admin

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.am.projectinternalresto.R
import com.am.projectinternalresto.data.response.super_admin.location.DataItemLocation

class SelectWorkLocationAdapter(
    context: Context, resource: Int, private val locationList: List<DataItemLocation>
) : ArrayAdapter<DataItemLocation>(context, R.layout.item_content_dropdown, locationList) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = convertView ?: LayoutInflater.from(context)
            .inflate(R.layout.item_content_dropdown, parent, false)
        val workLocation = locationList[position]
        val textView = view.findViewById<TextView>(R.id.textItem)
        textView.text = buildString { append(workLocation.outletName) }
        return view
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        return getView(position, convertView, parent)
    }

    override fun getItem(position: Int): DataItemLocation {
        return locationList[position]
    }

    override fun getItemId(position: Int): Long {
        return locationList[position].id.hashCode().toLong()
    }
}