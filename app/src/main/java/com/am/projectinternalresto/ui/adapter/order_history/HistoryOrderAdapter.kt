package com.am.projectinternalresto.ui.adapter.order_history

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.am.projectinternalresto.data.response.staff.order.DataItemListOrder
import com.am.projectinternalresto.databinding.ItemContentPaidOrderBinding

class HistoryOrderAdapter(private var onclickDetailOrder: ((idOrder: String) -> Unit)? = null) :
    ListAdapter<DataItemListOrder, HistoryOrderAdapter.ViewHolder>(DIFF_CALLBACK) {
    fun callbackClickDetailOrder(listener: (idOrder: String) -> Unit) {
        onclickDetailOrder = listener
    }

    inner class ViewHolder(private val binding: ItemContentPaidOrderBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(data: DataItemListOrder) {
            binding.textId.text = data.noOrder.toString()
            binding.textName.text = data.nameBuyer.toString()
            binding.textStatus.visibility = View.INVISIBLE
            binding.buttonChangeStatus.visibility = View.GONE
            binding.buttonToDetailOrder.setOnClickListener { onclickDetailOrder?.invoke(data.id.toString()) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemContentPaidOrderBinding.inflate(
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
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<DataItemListOrder>() {
            override fun areItemsTheSame(
                oldItem: DataItemListOrder, newItem: DataItemListOrder
            ): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(
                oldItem: DataItemListOrder, newItem: DataItemListOrder
            ): Boolean {
                return oldItem == newItem
            }
        }
    }
}