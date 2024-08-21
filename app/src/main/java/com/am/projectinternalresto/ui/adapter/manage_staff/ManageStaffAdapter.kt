package com.am.projectinternalresto.ui.adapter.manage_staff

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.am.projectinternalresto.data.response.admin.manage_staff.DataItemStaff
import com.am.projectinternalresto.databinding.ItemContentManageStaffBinding

class ManageStaffAdapter(
    private var onEditClick: ((data: DataItemStaff) -> Unit)? = null,
    private var onDeleteClick: ((idUser: String) -> Unit)? = null
) :
    ListAdapter<DataItemStaff, ManageStaffAdapter.ViewHolder>(DIFF_CALLBACK) {

    fun callbackOnEditClickListener(listener: (data: DataItemStaff) -> Unit) {
        onEditClick = listener
    }

    fun callbackOnDeleteClickListener(listener: (idUser: String) -> Unit) {
        onDeleteClick = listener
    }

    inner class ViewHolder(private val binding: ItemContentManageStaffBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(dataStaff: DataItemStaff) {
            binding.textName.text = dataStaff.name
            binding.textRole.text = dataStaff.role
            binding.textNumberPhone.text = dataStaff.phoneNumber
            binding.textLocation.text = dataStaff.locationId

            binding.action.apply {
                buttonEdit.setOnClickListener { onEditClick?.invoke(dataStaff) }
                buttonDelete.setOnClickListener { onDeleteClick?.invoke(dataStaff.id.toString()) }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemContentManageStaffBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val dataStaff = getItem(position)
        holder.bind(dataStaff)
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<DataItemStaff>() {
            override fun areItemsTheSame(
                oldItem: DataItemStaff, newItem: DataItemStaff
            ): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(
                oldItem: DataItemStaff, newItem: DataItemStaff
            ): Boolean {
                return oldItem == newItem
            }
        }
    }
}