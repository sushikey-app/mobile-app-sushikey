package com.am.projectinternalresto.ui.adapter.manage_category

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.am.projectinternalresto.data.model.DummyModel
import com.am.projectinternalresto.databinding.ItemContentManageCategoryBinding

class ManageCategoryAdapter :
    ListAdapter<DummyModel.DummyModelCategory, ManageCategoryAdapter.ViewHolder>(DIFF_CALLBACK) {
    var callbackOnClickEdit: (() -> Unit)? = null
    var callbackOnClickDelete: (() -> Unit)? = null

    inner class ViewHolder(private val binding: ItemContentManageCategoryBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(dataCategory: DummyModel.DummyModelCategory) {
            binding.textId.text = dataCategory.id
            binding.textNameCategory.text = dataCategory.name
            binding.action.apply {
                buttonEdit.setOnClickListener {
                    callbackOnClickEdit?.invoke()
                }
                buttonDelete.setOnClickListener {
                    callbackOnClickDelete?.invoke()
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
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<DummyModel.DummyModelCategory>() {
            override fun areItemsTheSame(
                oldItem: DummyModel.DummyModelCategory,
                newItem: DummyModel.DummyModelCategory
            ): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(
                oldItem: DummyModel.DummyModelCategory,
                newItem: DummyModel.DummyModelCategory
            ): Boolean {
                return oldItem == newItem
            }
        }
    }
}