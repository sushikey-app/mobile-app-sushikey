package com.am.projectinternalresto.ui.adapter.manage_location

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.am.projectinternalresto.data.response.super_admin.location.DataItemLocation
import com.am.projectinternalresto.databinding.ItemContentManageLocationBinding


class ManageLocationAdapter(
    private var onEditClick: ((data: DataItemLocation) -> Unit)? = null,
    private var onDeleteClick: ((id: String) -> Unit)? = null
) :
    ListAdapter<DataItemLocation, ManageLocationAdapter.ViewHolder>(DIFF_CALLBACK) {
    fun callbackOnEditClickListener(listener: (data: DataItemLocation) -> Unit) {
        onEditClick = listener
    }

    fun callbackOnDeleteClickListener(listener: (id: String) -> Unit) {
        onDeleteClick = listener
    }

    inner class ViewHolder(private val binding: ItemContentManageLocationBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(dataManageLocation: DataItemLocation, position: Int) {
            binding.number.text = position.plus(1).toString()
            binding.textNameResto.text = dataManageLocation.outletName
            binding.textLocation.text = dataManageLocation.locationOutlet
            binding.textNumberPhone.text = dataManageLocation.phoneNumber ?: "-"
            binding.action.buttonEdit.setOnClickListener {
                onEditClick?.invoke(dataManageLocation)
            }
            binding.action.buttonDelete.setOnClickListener {
                onDeleteClick?.invoke(dataManageLocation.id)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemContentManageLocationBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val dataManageLocation = getItem(position)
        holder.bind(dataManageLocation, position)
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<DataItemLocation>() {
            override fun areItemsTheSame(
                oldItem: DataItemLocation,
                newItem: DataItemLocation
            ): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(
                oldItem: DataItemLocation,
                newItem: DataItemLocation
            ): Boolean {
                return oldItem == newItem
            }
        }
    }
}