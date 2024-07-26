package com.am.projectinternalresto.ui.adapter.manage_menu

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.am.projectinternalresto.data.model.DummyModel
import com.am.projectinternalresto.databinding.ItemContentManageMenuBinding

class ManageMenuAdapter :
    ListAdapter<DummyModel.DummyModelMenu, ManageMenuAdapter.ViewHolder>(DIFF_CALLBACK) {
    var callbackOnClickEdit: (() -> Unit)? = null
    var callBackOnClickDelete: (() -> Unit)? = null

    inner class ViewHolder(private val binding: ItemContentManageMenuBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(dataMenu: DummyModel.DummyModelMenu) {
            binding.textNoMenu.text = dataMenu.numberMenu.toString()
            binding.textCategory.text = dataMenu.category
            binding.textNameProduct.text = dataMenu.nameProduct
            binding.textQuota.text = dataMenu.quota.toString()
            binding.textPrice.text = dataMenu.price
            binding.action.buttonEdit.setOnClickListener {
                callbackOnClickEdit?.invoke()
            }
            binding.action.buttonDelete.setOnClickListener {
                callBackOnClickDelete?.invoke()
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemContentManageMenuBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val dataMenu = getItem(position)
        holder.bind(dataMenu)
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<DummyModel.DummyModelMenu>() {
            override fun areItemsTheSame(
                oldItem: DummyModel.DummyModelMenu, newItem: DummyModel.DummyModelMenu
            ): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(
                oldItem: DummyModel.DummyModelMenu, newItem: DummyModel.DummyModelMenu
            ): Boolean {
                return oldItem == newItem
            }
        }
    }
}