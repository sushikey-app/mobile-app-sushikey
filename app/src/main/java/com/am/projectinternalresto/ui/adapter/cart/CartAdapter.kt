package com.am.projectinternalresto.ui.adapter.cart

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.am.projectinternalresto.data.model.DummyModel
import com.am.projectinternalresto.databinding.ItemContentOrderInformationCartBinding
import com.bumptech.glide.Glide

class CartAdapter : ListAdapter<DummyModel.DummyModelCart, CartAdapter.ViewHolder>(DIFF_CALLBACK) {
    var callbackOnClickDelete: (() -> Unit)? = null
    var callbackOnClickPlus: (() -> Unit)? = null
    var callbackOnclickMinus: (() -> Unit)? = null

    inner class ViewHolder(private val binding: ItemContentOrderInformationCartBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(dataCart: DummyModel.DummyModelCart) {
            Glide.with(binding.root.context).load(dataCart.image)
                .into(binding.itemMenuOrder.imageMenu)
            binding.itemMenuOrder.textValueTopping.text = dataCart.topping
            binding.itemMenuOrder.textValueNote.text = dataCart.note
            binding.textPrice.text = dataCart.price
            binding.textQty.text = dataCart.qty.toString()
            binding.textTotal.text = dataCart.total
            binding.buttonDelete.setOnClickListener {
                callbackOnClickDelete?.invoke()
            }
            binding.buttonPlus.setOnClickListener {
                callbackOnClickPlus?.invoke()
            }
            binding.buttonMinus.setOnClickListener {
                callbackOnclickMinus?.invoke()
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartAdapter.ViewHolder {
        return ViewHolder(
            ItemContentOrderInformationCartBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: CartAdapter.ViewHolder, position: Int) {
        val dataCart = getItem(position)
        holder.bind(dataCart)
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