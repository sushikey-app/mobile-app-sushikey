package com.am.projectinternalresto.ui.adapter.manage_category

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.am.projectinternalresto.data.response.admin.category.DataItemCategory
import com.am.projectinternalresto.databinding.ItemContentManageCategoryBinding

class ManageCategoryAdapter(
    private var onEditClick: ((data: DataItemCategory) -> Unit)? = null,
    private var onDeleteClick: ((idCategory: String) -> Unit)? = null
) :
    ListAdapter<DataItemCategory, ManageCategoryAdapter.ViewHolder>(DIFF_CALLBACK) {
    fun callbackOnEditClickListener(listener: (data: DataItemCategory) -> Unit) {
        onEditClick = listener
    }

    fun callbackOnDeleteClickListener(listener: (idCategory: String) -> Unit) {
        onDeleteClick = listener
    }

    inner class ViewHolder(private val binding: ItemContentManageCategoryBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(dataCategory: DataItemCategory) {
            binding.textCodeCategory.text = dataCategory.codeCategory
            binding.textNameCategory.text = dataCategory.nameCategory
            binding.action.apply {
                buttonEdit.setOnClickListener {
                    onEditClick?.invoke(dataCategory)
                }
                buttonDelete.setOnClickListener {
                    onDeleteClick?.invoke(dataCategory.id)
                }
            }

        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemContentManageCategoryBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val dataCategory = getItem(position)
        holder.bind(dataCategory)
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<DataItemCategory>() {
            override fun areItemsTheSame(
                oldItem: DataItemCategory,
                newItem: DataItemCategory
            ): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(
                oldItem: DataItemCategory,
                newItem: DataItemCategory
            ): Boolean {
                return oldItem == newItem
            }
        }
    }
}