package com.am.projectinternalresto.ui.adapter.menu

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.am.projectinternalresto.data.model.DummyModel
import com.am.projectinternalresto.databinding.ItemContentMenuBinding
import com.bumptech.glide.Glide

class MenuAdapter : ListAdapter<DummyModel.DummyModelMenu, MenuAdapter.ViewHolder>(DIFF_CALLBACK) {
    var callbackAddToCart: (() -> Unit)? = null

    inner class ViewHolder(private val binding: ItemContentMenuBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(dataMenu: DummyModel.DummyModelMenu) {
            Glide.with(binding.root.context).load(dataMenu.image).into(binding.imageMenu)
            binding.textCategory.text = dataMenu.category
            binding.textName.text = dataMenu.nameProduct
            binding.textValueQuota.text = dataMenu.quota.toString()
            binding.textValuePrice.text = dataMenu.price
            binding.buttonAddToCart.setOnClickListener {
                callbackAddToCart?.invoke()
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
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<DummyModel.DummyModelMenu>() {
            override fun areItemsTheSame(
                oldItem: DummyModel.DummyModelMenu, newItem: DummyModel.DummyModelMenu
            ): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(
                oldItem: DummyModel.DummyModelMenu, newItem: DummyModel.DummyModelMenu
            ): Boolean {
                return oldItem == newItem
            }
        }
    }

}