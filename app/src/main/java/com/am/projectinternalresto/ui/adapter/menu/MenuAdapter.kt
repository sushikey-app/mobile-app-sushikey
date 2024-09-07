package com.am.projectinternalresto.ui.adapter.menu

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.am.projectinternalresto.data.response.admin.menu.DataItemMenu
import com.am.projectinternalresto.databinding.ItemContentMenuBinding
import com.am.projectinternalresto.utils.Formatter
import com.bumptech.glide.Glide

class MenuAdapter(private var onClickAddToCart: ((data: DataItemMenu) -> Unit)? = null) :
    ListAdapter<DataItemMenu, MenuAdapter.ViewHolder>(DIFF_CALLBACK) {
    fun callbackOnClickListener(listener: (data: DataItemMenu) -> Unit) {
        onClickAddToCart = listener
    }

    inner class ViewHolder(private val binding: ItemContentMenuBinding) :
        RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")
        fun bind(dataMenu: DataItemMenu) {
            Glide.with(binding.root.context).load(dataMenu.imageMenu).into(binding.imageMenu)
            binding.textCategory.text = dataMenu.category?.nameCategory
            binding.textName.text = dataMenu.nameMenu
            binding.textQuota.text = Formatter.formatQuantity(dataMenu.quota ?: 0)
            binding.textPrice.text = Formatter.formatCurrency(dataMenu.price ?: 0)
            binding.root.setOnClickListener {
                onClickAddToCart?.invoke(dataMenu)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemContentMenuBinding.inflate(
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