package com.am.projectinternalresto.ui.adapter.manage_location

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.am.projectinternalresto.data.model.DummyModel
import com.am.projectinternalresto.databinding.ItemContentManageLocationBinding


class ManageLocationAdapter :
    ListAdapter<DummyModel.DummyModelManageLocation, ManageLocationAdapter.ViewHolder>(DIFF_CALLBACK) {
    var onClickButtonEdit: (() -> Unit)? = null
    var onClickButtonDelete: (() -> Unit)? = null

    inner class ViewHolder(private val binding: ItemContentManageLocationBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(dataManageLocation: DummyModel.DummyModelManageLocation) {
            binding.number.text = dataManageLocation.no.toString()
            binding.textNameResto.text = dataManageLocation.restoName
            binding.textLocation.text = dataManageLocation.restoLocation
            binding.textNumberPhone.text = dataManageLocation.numberTelephone
            binding.action.buttonEdit.setOnClickListener {
                onClickButtonEdit?.invoke()
            }
            binding.action.buttonDelete.setOnClickListener {
                onClickButtonDelete?.invoke()
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
        holder.bind(dataManageLocation)
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<DummyModel.DummyModelManageLocation>() {
            override fun areItemsTheSame(
                oldItem: DummyModel.DummyModelManageLocation,
                newItem: DummyModel.DummyModelManageLocation
            ): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(
                oldItem: DummyModel.DummyModelManageLocation,
                newItem: DummyModel.DummyModelManageLocation
            ): Boolean {
                return oldItem == newItem
            }
        }
    }
}