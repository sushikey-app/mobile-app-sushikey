package com.am.projectinternalresto.ui.adapter.manage_admin

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.am.projectinternalresto.R
import com.am.projectinternalresto.data.response.admin.category.DataItemCategory

class SelectCategoryMenuAdapter(
    context: Context, private val categoryMenuList: List<DataItemCategory>
) : ArrayAdapter<DataItemCategory>(context, R.layout.item_content_dropdown, categoryMenuList) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = convertView ?: LayoutInflater.from(context)
            .inflate(R.layout.item_content_dropdown, parent, false)
        val categoryMenu = categoryMenuList[position]
        val textView = view.findViewById<TextView>(R.id.textItem)
        textView.text = buildString { append(categoryMenu.nameCategory) }
        return view
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        return getView(position, convertView, parent)
    }

    override fun getItem(position: Int): DataItemCategory {
        return categoryMenuList[position]
    }

    override fun getItemId(position: Int): Long {
        return categoryMenuList[position].id.hashCode().toLong()
    }
}