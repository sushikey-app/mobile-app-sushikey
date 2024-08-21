package com.am.projectinternalresto.ui.adapter.manage_location

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.am.projectinternalresto.data.model.DummyModel
import com.am.projectinternalresto.databinding.ItemContentSelectCheckboxBinding

class SelectLocationAdapter : RecyclerView.Adapter<SelectLocationAdapter.ViewHolder>() {
    inner class ViewHolder(private val binding: ItemContentSelectCheckboxBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(dataSelectMenu: DummyModel.DummyModelSelectLocation) {
            binding.checkbox.text = dataSelectMenu.dataLocation.restoName
            binding.checkbox.isChecked = dataSelectMenu.isChecked
            binding.checkbox.setOnCheckedChangeListener { _, isChecked ->
                dataSelectMenu.isChecked = isChecked
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemContentSelectCheckboxBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    val itemList = arrayListOf<Any>()

    override fun getItemCount(): Int = itemList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(itemList[position] as DummyModel.DummyModelSelectLocation)
    }

    fun updateData(updateDate: List<Any>) {
        itemList.clear()
        itemList.addAll(updateDate)
        notifyDataSetChanged()
    }
}