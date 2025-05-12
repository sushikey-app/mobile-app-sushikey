package com.am.projectinternalresto.ui.adapter.order

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.am.projectinternalresto.R
import com.am.projectinternalresto.data.response.staff.order.DataItemListOrder
import com.am.projectinternalresto.databinding.ItemContentPaidOrderBinding
import com.am.projectinternalresto.utils.Key

class OrderAdapter(
    private var onClickToDetailOrder: ((idOrder: String) -> Unit)? = null,
    private var onClickChangeStatus: ((idOrder: String) -> Unit)? = null,
    private var onClickCancel: ((idOrder: String) -> Unit)? = null,
    private var onClickPrint: ((idOrder: String) -> Unit)? = null

) : ListAdapter<DataItemListOrder, OrderAdapter.ViewHolder>(DIFF_CALLBACK) {
    fun callbackOnClickToDetailOrderListener(listener: (idOrder: String) -> Unit) {
        onClickToDetailOrder = listener
    }

    fun callbackOnclickChangeStatusListener(listener: (idOrder: String) -> Unit) {
        onClickChangeStatus = listener
    }

    fun callbackOnclickCancelListener(listener: (idOrder: String) -> Unit) {
        onClickCancel = listener
    }

    fun callbackOnclickPrint(listener: (idOrder: String) -> Unit) {
        onClickPrint = listener
    }

    inner class ViewHolder(private val binding: ItemContentPaidOrderBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(data: DataItemListOrder) {
            binding.textId.text = data.noOrder.toString()
            binding.textName.text = data.nameBuyer.toString()
            binding.textStatus.text =
                if (data.reasonStatus != null) data.reasonStatus.toString() else data.statusOrder.toString()
            binding.buttonToDetailOrder.setOnClickListener { onClickToDetailOrder?.invoke(data.id.toString()) }
            binding.buttonChangeStatus.setOnClickListener { onClickChangeStatus?.invoke(data.id.toString()) }
            binding.buttonCancelOrders.setOnClickListener { onClickCancel?.invoke(data.id.toString()) }
            binding.buttonPrint.setOnClickListener { onClickPrint?.invoke(data.id.toString()) }
            if (data.statusPayment == Key.IS_UNPAID_ORDER) {
                binding.buttonToDetailOrder.text =
                    binding.root.context.getString(R.string.edit_order)
                binding.buttonChangeStatus.text = binding.root.context.getString(R.string.pay)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemContentPaidOrderBinding.inflate(
                LayoutInflater.from(
                    parent.context
                ), parent, false
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