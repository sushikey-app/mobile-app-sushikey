package com.am.projectinternalresto.ui.adapter.manage_admin

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.am.projectinternalresto.data.response.super_admin.manage_admin.DataItemManageAdminAndSuperAdmin
import com.am.projectinternalresto.databinding.ItemContentManageStaffBinding

class ManageAdminAdapter(
    private var onEditClick: ((data: DataItemManageAdminAndSuperAdmin) -> Unit)? = null,
    private var onDeleteClick: ((idUser: String) -> Unit)? = null
) :
    ListAdapter<DataItemManageAdminAndSuperAdmin, ManageAdminAdapter.ViewHolder>(DIFF_CALLBACK) {

    fun callbackOnEditClickListener(listener: (data: DataItemManageAdminAndSuperAdmin) -> Unit) {
        onEditClick = listener
    }

    fun callbackOnDeleteClickListener(listener: (idUser: String) -> Unit) {
        onDeleteClick = listener
    }

    inner class ViewHolder(private val binding: ItemContentManageStaffBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(dataManageAdmin: DataItemManageAdminAndSuperAdmin) {
            binding.textName.text = dataManageAdmin.name
            binding.textRole.text = dataManageAdmin.role
            binding.textNumberPhone.text = dataManageAdmin.phoneNumber ?: "-"
            binding.textLocation.text = dataManageAdmin.location?.outletName ?: "Global"

            binding.action.apply {
                buttonEdit.setOnClickListener { onEditClick?.invoke(dataManageAdmin) }
                buttonDelete.setOnClickListener { onDeleteClick?.invoke(dataManageAdmin.id.toString()) }
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
        val dataManageAdmin = getItem(position)
        holder.bind(dataManageAdmin)
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<DataItemManageAdminAndSuperAdmin>() {
            override fun areItemsTheSame(
                oldItem: DataItemManageAdminAndSuperAdmin, newItem: DataItemManageAdminAndSuperAdmin
            ): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(
                oldItem: DataItemManageAdminAndSuperAdmin, newItem: DataItemManageAdminAndSuperAdmin
            ): Boolean {
                return oldItem == newItem
            }
        }
    }
}