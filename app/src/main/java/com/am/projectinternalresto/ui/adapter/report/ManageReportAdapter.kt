package com.am.projectinternalresto.ui.adapter.report

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.am.projectinternalresto.data.model.DummyModel
import com.am.projectinternalresto.databinding.ItemContentReportBinding

class ManageReportAdapter :
    ListAdapter<DummyModel.DummyModelReport, ManageReportAdapter.ViewHolder>(DIFF_CALLBACK) {
    var onClickDetail: (() -> Unit)? = null

    inner class ViewHolder(private val binding: ItemContentReportBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(dataReport: DummyModel.DummyModelReport) {
            binding.number.text = dataReport.no.toString()
            binding.textOrderDate.text = dataReport.Date
            binding.textNumberOrder.text = dataReport.numberOrder
            binding.textTotal.text = dataReport.total
            binding.textLocation.text = dataReport.location
            binding.buttonToDetail.setOnClickListener {
                onClickDetail?.invoke()
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
        holder.bind(dataReport)
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<DummyModel.DummyModelReport>() {
            override fun areItemsTheSame(
                oldItem: DummyModel.DummyModelReport, newItem: DummyModel.DummyModelReport
            ): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(
                oldItem: DummyModel.DummyModelReport, newItem: DummyModel.DummyModelReport
            ): Boolean {
                return oldItem == newItem
            }
        }
    }

}