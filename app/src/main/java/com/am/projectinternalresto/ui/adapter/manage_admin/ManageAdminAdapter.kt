package com.am.projectinternalresto.ui.adapter.manage_admin

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.am.projectinternalresto.data.model.DummyModel
import com.am.projectinternalresto.databinding.ItemContentManageStaffBinding

class ManageAdminAdapter(
    private var onEditClick: (() -> Unit)? = null,
    private var onDeleteClick: (() -> Unit)? = null
) :
    ListAdapter<DummyModel.DummyModelManageAdmin, ManageAdminAdapter.ViewHolder>(DIFF_CALLBACK) {

    fun callbackOnEditClickListener(listener: () -> Unit) {
        onEditClick = listener
    }

    fun callbackOnDeleteClickListener(listener: () -> Unit) {
        onDeleteClick = listener
    }

    inner class ViewHolder(private val binding: ItemContentManageStaffBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(dataManageAdmin: DummyModel.DummyModelManageAdmin) {
            binding.textName.text = dataManageAdmin.name
            binding.textRole.text = dataManageAdmin.division
            binding.textNumberPhone.text = dataManageAdmin.phoneNumber
            binding.textLocation.text = dataManageAdmin.location

            binding.action.apply {
                buttonEdit.setOnClickListener { onEditClick?.invoke() }
                buttonDelete.setOnClickListener { onDeleteClick?.invoke() }
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
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<DummyModel.DummyModelManageAdmin>() {
            override fun areItemsTheSame(
                oldItem: DummyModel.DummyModelManageAdmin, newItem: DummyModel.DummyModelManageAdmin
            ): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(
                oldItem: DummyModel.DummyModelManageAdmin, newItem: DummyModel.DummyModelManageAdmin
            ): Boolean {
                return oldItem == newItem
            }
        }
    }
}