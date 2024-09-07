package com.am.projectinternalresto.ui.adapter.cart

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.am.projectinternalresto.data.model.DummyModel
import com.am.projectinternalresto.databinding.ItemContentOrderInformationBinding
import com.am.projectinternalresto.utils.Formatter.formatCurrency
import com.bumptech.glide.Glide

@Suppress("UNCHECKED_CAST")
class CartAdapter(
    private val onQuantityChanged: (itemId: String, increment: Boolean) -> Unit

) : ListAdapter<DummyModel.CartItem, CartAdapter.ViewHolder>(DIFF_CALLBACK) {

    inner class ViewHolder(private val binding: ItemContentOrderInformationBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(dataCart: DummyModel.CartItem) {
            val toppingText = dataCart.selectedToppings.joinToString(", ") { it.nama ?: "" }
            val toppingPrice = dataCart.selectedToppings.sumOf { it.harga ?: 0 }
            val totalItemPrice = (dataCart.menuItem.price ?: 0) + toppingPrice

            Glide.with(binding.root.context).load(dataCart.menuItem.imageMenu)
                .into(binding.itemMenuOrder.imageMenu)
            binding.itemMenuOrder.apply {
                textNameMenu.text = dataCart.menuItem.nameMenu
                textValueTopping.text = toppingText.ifEmpty { "-" }
                textValueNote.text = dataCart.note.ifEmpty { "-" }
                textPriceItemMenu.text = formatCurrency(dataCart.menuItem.price ?: 0)
            }
            binding.textQty.text = dataCart.qty.toString()
            binding.textPrice.text = formatCurrency(totalItemPrice * dataCart.qty)
            binding.buttonPlus.setOnClickListener {
                onQuantityChanged(dataCart.menuItem.idMenu.toString(), true)
            }
            binding.buttonMinus.setOnClickListener {
                onQuantityChanged(dataCart.menuItem.idMenu.toString(), false)
            }
        }

        fun updateQuantityAndPrice(newQty: Int, pricePerItem: Int?) {
            binding.textQty.text = newQty.toString()
            binding.textPrice.text = formatCurrency(pricePerItem?.times(newQty) ?: 0)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartAdapter.ViewHolder {
        return ViewHolder(
            ItemContentOrderInformationBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int, payloads: MutableList<Any>) {
        if (payloads.isNotEmpty()) {
            val data = payloads[0] as Pair<Int, Int>
            val newQty = data.first
            val pricePerItem = data.second
            holder.updateQuantityAndPrice(newQty, pricePerItem)
        } else {
            super.onBindViewHolder(holder, position, payloads)
        }
    }

    override fun onBindViewHolder(holder: CartAdapter.ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<DummyModel.CartItem>() {
            override fun areItemsTheSame(
                oldItem: DummyModel.CartItem,
                newItem: DummyModel.CartItem
            ): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(
                oldItem: DummyModel.CartItem,
                newItem: DummyModel.CartItem
            ): Boolean {
                return oldItem == newItem
            }
        }
    }

}