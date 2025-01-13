package com.am.projectinternalresto.ui.adapter.manage_location

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.am.projectinternalresto.data.response.admin.menu.ToppingItem
import com.am.projectinternalresto.databinding.ItemContentSelectCheckboxBinding
import com.am.projectinternalresto.utils.Formatter

class SelectToppingAdapter :
    ListAdapter<ToppingItem, SelectToppingAdapter.ViewHolder>(DIFF_CALLBACK) {
    inner class ViewHolder(private val binding: ItemContentSelectCheckboxBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(dataSelectTopping: ToppingItem) {
            binding.checkbox.text = buildString {
                append(dataSelectTopping.nama)
                append(" ")
                append("(${Formatter.formatCurrency(dataSelectTopping.harga ?: 0)})")
            }
            binding.checkbox.isChecked = dataSelectTopping.isSelected
            binding.checkbox.setOnCheckedChangeListener { _, isSelected ->
                dataSelectTopping.isSelected = isSelected
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemContentSelectCheckboxBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = getItem(position)
        holder.bind(data)
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<ToppingItem>() {
            override fun areItemsTheSame(
                oldItem: ToppingItem,
                newItem: ToppingItem
            ): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(
                oldItem: ToppingItem,
                newItem: ToppingItem
            ): Boolean {
                return oldItem == newItem
            }
        }
    }
}