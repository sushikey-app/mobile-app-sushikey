package com.am.projectinternalresto.ui.adapter.report

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.am.projectinternalresto.data.response.super_admin.report.DataItemListReport
import com.am.projectinternalresto.databinding.ItemContentReportBinding
import com.am.projectinternalresto.utils.Formatter

class ManageReportAdapter(private var onClickToDetail: ((idOrder: String) -> Unit)? = null) :
    ListAdapter<DataItemListReport, ManageReportAdapter.ViewHolder>(DIFF_CALLBACK) {
    fun callbackOnclickToDetail(listener: (String) -> Unit) {
        onClickToDetail = listener
    }

    inner class ViewHolder(private val binding: ItemContentReportBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(dataReport: DataItemListReport, position: Int) {
            binding.textOrderDate.text = dataReport.dateOrder
            binding.textNumberOrder.text = dataReport.noOrder
            binding.textTotal.text = Formatter.formatCurrency(dataReport.totalPrice ?: 0)
            binding.textStatus.text = dataReport.statusOrder
            binding.textLocation.text = dataReport.location?.namaResto
            binding.buttonToDetail.setOnClickListener {
                onClickToDetail?.invoke(dataReport.id.toString())
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemContentReportBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val dataReport = getItem(position)
        holder.bind(dataReport, position)
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<DataItemListReport>() {
            override fun areItemsTheSame(
                oldItem: DataItemListReport, newItem: DataItemListReport
            ): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(
                oldItem: DataItemListReport, newItem: DataItemListReport
            ): Boolean {
                return oldItem == newItem
            }
        }
    }

}