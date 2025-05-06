package com.am.projectinternalresto.ui.adapter.order

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.am.projectinternalresto.data.response.super_admin.cancel_order.DataItemCancelOrder
import com.am.projectinternalresto.databinding.ItemContentCancelOrderBinding

class CancelOrderAdapter(
    var isStaff: Boolean = false,
    private var onClickYes: ((idOrder: String) -> Unit)? = null,
    private var onClickNo: ((idOrder: String) -> Unit)? = null,
    private var onClickDetailOrder: ((idOrder: String) -> Unit)? = null,
) : ListAdapter<DataItemCancelOrder, CancelOrderAdapter.ViewHolder>(DIFF_CALLBACK) {

    fun callbackClickYes(listener: (idOrder: String) -> Unit) {
        onClickYes = listener
    }

    fun callbackClickNo(listener: (idOrder: String) -> Unit) {
        onClickNo = listener
    }

    fun callbackClickDetailOrder(listener: (idOrder: String) -> Unit) {
        onClickDetailOrder = listener
    }

    inner class ViewHolder(private val binding: ItemContentCancelOrderBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(data: DataItemCancelOrder) {
            binding.textId.text = data.nomorOrder
            binding.textName.text = data.lokasi?.namaResto
            binding.textCustomerName.text = data.customerName.toString()
            binding.textStatus.text = data.statusCancel.toString()
            if (isStaff) {
                binding.buttonCancelNo.visibility = View.GONE
                binding.buttonCancelYes.visibility = View.GONE
            } else {
                binding.buttonCancelNo.visibility = View.VISIBLE
                binding.buttonCancelYes.visibility = View.VISIBLE
            }
            binding.buttonDetailOrder.setOnClickListener { onClickDetailOrder?.invoke(data.id.toString()) }
            binding.buttonCancelYes.setOnClickListener { onClickYes?.invoke(data.id.toString()) }
            binding.buttonCancelNo.setOnClickListener { onClickNo?.invoke(data.id.toString()) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemContentCancelOrderBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<DataItemCancelOrder>() {
            override fun areItemsTheSame(
                oldItem: DataItemCancelOrder, newItem: DataItemCancelOrder
            ): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(
                oldItem: DataItemCancelOrder, newItem: DataItemCancelOrder
            ): Boolean {
                return oldItem == newItem
            }
        }
    }
}