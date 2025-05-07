package com.am.projectinternalresto.ui.adapter.menu

import android.annotation.SuppressLint
import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
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
    private var stockMap: Map<String, Int> = emptyMap()

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

            val originalPrice = dataMenu.price ?: 0
            val discount = dataMenu.disc ?: 0 // pastikan `discount` adalah nullable Int?

            if (discount > 0) {
                val discountedPrice = originalPrice - discount

                binding.textPriceDisc.apply {
                    visibility = View.VISIBLE
                    text = Formatter.formatCurrency(discountedPrice)
                }

                binding.textPrice.apply {
                    text = Formatter.formatCurrency(originalPrice)
                    paintFlags = paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
                }
            } else {
                binding.textPriceDisc.visibility = View.GONE
                binding.textPrice.apply {
                    text = Formatter.formatCurrency(originalPrice)
                    paintFlags =
                        paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv() // hapus coret jika sebelumnya aktif
                }
            }

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