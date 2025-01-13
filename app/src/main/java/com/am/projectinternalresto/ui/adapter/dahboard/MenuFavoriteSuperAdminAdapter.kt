package com.am.projectinternalresto.ui.adapter.dahboard

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.am.projectinternalresto.data.response.super_admin.dashboard.DataItemMenuFavorite
import com.am.projectinternalresto.databinding.ItemContentDashboardMenuFavoriteBinding

class MenuFavoriteSuperAdminAdapter :
    ListAdapter<DataItemMenuFavorite, MenuFavoriteSuperAdminAdapter.ViewHolder>(
        DIFF_CALLBACK
    ) {

    inner class ViewHolder(private val binding: ItemContentDashboardMenuFavoriteBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(menuFavorite: DataItemMenuFavorite, position: Int) {
            binding.textNo.text = (position + 1).toString()
            binding.textNameMenu.text = menuFavorite.nameMenu
            binding.textTotalSales.text = menuFavorite.totalSales.toString()
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
        holder.bind(dataMenuFavorite, position)
    }


    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<DataItemMenuFavorite>() {
            override fun areItemsTheSame(
                oldItem: DataItemMenuFavorite,
                newItem: DataItemMenuFavorite
            ): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(
                oldItem: DataItemMenuFavorite,
                newItem: DataItemMenuFavorite
            ): Boolean {
                return oldItem == newItem
            }
        }
    }
}