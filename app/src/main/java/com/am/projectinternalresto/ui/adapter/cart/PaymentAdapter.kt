package com.am.projectinternalresto.ui.adapter.cart

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.am.projectinternalresto.data.model.DummyModel
import com.am.projectinternalresto.databinding.ItemContentPaymentBinding

class PaymentAdapter :
    ListAdapter<DummyModel.DummyModelCart, PaymentAdapter.ViewHolder>(DIFF_CALLBACK) {
    class ViewHolder(private val binding: ItemContentPaymentBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(dataPayment: DummyModel.DummyModelCart) {
            binding.textItemCart.text = dataPayment.name
            binding.textQty.text = dataPayment.qty.toString()
            binding.textTotal.text = dataPayment.total

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PaymentAdapter.ViewHolder {
        return ViewHolder(
            ItemContentPaymentBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: PaymentAdapter.ViewHolder, position: Int) {
        val dataPayment = getItem(position)
        holder.bind(dataPayment)
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<DummyModel.DummyModelCart>() {
            override fun areItemsTheSame(
                oldItem: DummyModel.DummyModelCart,
                newItem: DummyModel.DummyModelCart
            ): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(
                oldItem: DummyModel.DummyModelCart,
                newItem: DummyModel.DummyModelCart
            ): Boolean {
                return oldItem == newItem
            }
        }
    }
}