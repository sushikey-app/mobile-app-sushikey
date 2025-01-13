package com.am.projectinternalresto.ui.adapter.manage_menu

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.am.projectinternalresto.data.response.admin.menu.DataItemMenu
import com.am.projectinternalresto.databinding.ItemContentManageMenuBinding
import com.am.projectinternalresto.utils.Formatter

class ManageMenuAdapter(
    private var onEditClick: ((data: DataItemMenu) -> Unit)? = null,
    private var onDeleteClick: ((idMenu: String) -> Unit)? = null
) :
    ListAdapter<DataItemMenu, ManageMenuAdapter.ViewHolder>(DIFF_CALLBACK) {

    fun callbackOnEditClickListener(listener: (data: DataItemMenu) -> Unit) {
        onEditClick = listener
    }

    fun callbackOnDeleteClickListener(listener: (idMenu: String) -> Unit) {
        onDeleteClick = listener
    }

    inner class ViewHolder(private val binding: ItemContentManageMenuBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(dataMenu: DataItemMenu) {
            binding.textNoMenu.text = dataMenu.noMenu.toString()
            binding.textCategory.text = dataMenu.category?.nameCategory.toString()
            binding.textNameProduct.text = dataMenu.nameMenu.toString()
//            binding.textQuota.text = dataMenu.quota.toString()
            binding.textPrice.text = Formatter.formatCurrency(dataMenu.price ?: 0)
            binding.action.buttonEdit.setOnClickListener {
                onEditClick?.invoke(dataMenu)
            }
            binding.action.buttonDelete.setOnClickListener {
                onDeleteClick?.invoke(dataMenu.idMenu.toString())
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
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<DataItemMenu>() {
            override fun areItemsTheSame(
                oldItem: DataItemMenu, newItem: DataItemMenu
            ): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(
                oldItem: DataItemMenu, newItem: DataItemMenu
            ): Boolean {
                return oldItem == newItem
            }
        }
    }
}