package com.am.projectinternalresto.ui.adapter.report

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.am.projectinternalresto.data.response.super_admin.report.OrderItems
import com.am.projectinternalresto.databinding.ItemContentOrderInformationBinding
import com.am.projectinternalresto.utils.Formatter
import com.bumptech.glide.Glide

class DetailReportOrderAdapter :
    ListAdapter<OrderItems, DetailReportOrderAdapter.ViewHolder>(DIFF_CALLBACK) {
    inner class ViewHolder(private val binding: ItemContentOrderInformationBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(data: OrderItems) {
            val toppingText = data.topping?.joinToString(", ") { it?.nama ?: "" }
            val toppingPrice = data.topping?.sumOf { it?.harga ?: 0 }
            val price = data.menu?.discPrice ?: data.menu?.harga
            val totalItemPrice = (price ?: 0) + toppingPrice!!

            binding.buttonMinus.visibility = View.INVISIBLE
            binding.buttonPlus.visibility = View.INVISIBLE

            Glide.with(binding.root).load(data.menu?.image.toString())
                .into(binding.itemMenuOrder.imageMenu)
            binding.itemMenuOrder.textNameMenu.text = data.menu?.nama
            binding.textQty.text = data.qty.toString()
            binding.itemMenuOrder.textPriceItemMenu.text =
                Formatter.formatCurrency(price ?: 0)
            binding.itemMenuOrder.textValueTopping.text = toppingText
            binding.itemMenuOrder.textValueNote.text = data.note
            binding.textPrice.text = Formatter.formatCurrency(totalItemPrice)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemContentOrderInformationBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<OrderItems>() {
            override fun areItemsTheSame(
                oldItem: OrderItems, newItem: OrderItems
            ): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(
                oldItem: OrderItems, newItem: OrderItems
            ): Boolean {
                return oldItem == newItem
            }
        }
    }
}