package com.am.projectinternalresto.ui.adapter.dahboard

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.am.projectinternalresto.data.model.DummyModel
import com.am.projectinternalresto.databinding.ItemContentDashboardMenuFavoriteBinding

class MenuFavoriteAdapter :
    ListAdapter<DummyModel.DummyModelMenuFavorite, MenuFavoriteAdapter.ViewHolder>(DIFF_CALLBACK) {

    inner class ViewHolder(private val binding: ItemContentDashboardMenuFavoriteBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(menuFavorite: DummyModel.DummyModelMenuFavorite) {
            binding.textNameMenu.text = menuFavorite.nameMenu
            binding.textLocationOutlet.text = menuFavorite.locationOutlet
            binding.textTotalSales.text = menuFavorite.totalSales
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemContentDashboardMenuFavoriteBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val dataMenuFavorite = getItem(position)
        holder.bind(dataMenuFavorite)
    }


    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<DummyModel.DummyModelMenuFavorite>() {
            override fun areItemsTheSame(
                oldItem: DummyModel.DummyModelMenuFavorite,
                newItem: DummyModel.DummyModelMenuFavorite
            ): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(
                oldItem: DummyModel.DummyModelMenuFavorite,
                newItem: DummyModel.DummyModelMenuFavorite
            ): Boolean {
                return oldItem == newItem
            }
        }
    }
}