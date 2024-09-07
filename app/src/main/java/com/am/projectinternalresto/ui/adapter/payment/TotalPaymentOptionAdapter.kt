package com.am.projectinternalresto.ui.adapter.payment

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.am.projectinternalresto.databinding.ItemContentTotalPaymentBinding

class TotalPaymentOptionAdapter(private var onClick: ((data: String) -> Unit)? = null) :
    ListAdapter<String, TotalPaymentOptionAdapter.ViewHolder>(DIFF_CALLBACK) {

    fun callbackOnClickListener(listener: (data: String) -> Unit) {
        onClick = listener
    }

    inner class ViewHolder(private val binding: ItemContentTotalPaymentBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(optionTotalPayment: String) {
            binding.buttonOption.text = optionTotalPayment
            binding.buttonOption.setOnClickListener { onClick?.invoke(optionTotalPayment) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemContentTotalPaymentBinding.inflate(
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
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<String>() {
            override fun areItemsTheSame(
                oldItem: String, newItem: String
            ): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(
                oldItem: String, newItem: String
            ): Boolean {
                return oldItem == newItem
            }
        }
    }
}