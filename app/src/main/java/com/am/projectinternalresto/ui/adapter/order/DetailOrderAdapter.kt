package com.am.projectinternalresto.ui.adapter.order

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.am.projectinternalresto.data.response.staff.order.OrderItem
import com.am.projectinternalresto.databinding.ItemContentOrderInformationBinding
import com.am.projectinternalresto.utils.Formatter
import com.bumptech.glide.Glide

class DetailOrderAdapter :
    ListAdapter<OrderItem, DetailOrderAdapter.ViewHolder>(DIFF_CALLBACK) {
    inner class ViewHolder(private val binding: ItemContentOrderInformationBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(data: OrderItem) {
            val toppingText = data.topping?.joinToString(", ") { it?.nama ?: "" }
            val toppingPrice = data.topping?.sumOf { it?.harga ?: 0 }
            val totalItemPrice = (data.menu?.harga ?: 0) + toppingPrice!!

            binding.buttonMinus.visibility = View.INVISIBLE
            binding.buttonPlus.visibility = View.INVISIBLE
            Glide.with(binding.root).load(data.menu?.image.toString())
                .into(binding.itemMenuOrder.imageMenu)
            binding.itemMenuOrder.textNameMenu.text = data.menu?.nama.toString()
            binding.textQty.text = data.qty.toString()
            binding.itemMenuOrder.textPriceItemMenu.text =
                Formatter.formatCurrency(data.menu?.harga ?: 0)
            binding.itemMenuOrder.textValueTopping.text = toppingText
            binding.itemMenuOrder.textValueNote.text = data.note
            binding.textPrice.text = Formatter.formatCurrency(totalItemPrice ?: 0)
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
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<OrderItem>() {
            override fun areItemsTheSame(
                oldItem: OrderItem, newItem: OrderItem
            ): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(
                oldItem: OrderItem, newItem: OrderItem
            ): Boolean {
                return oldItem == newItem
            }
        }
    }
}