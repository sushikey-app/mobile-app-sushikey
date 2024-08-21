package com.am.projectinternalresto.ui.feature.super_admin.report

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.am.projectinternalresto.data.dummy.DummyData
import com.am.projectinternalresto.databinding.FragmentReportBinding
import com.am.projectinternalresto.ui.adapter.report.ManageReportAdapter


class SuperAdminReportFragment : Fragment() {
    private var _binding: FragmentReportBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentReportBinding.inflate(inflater, container, false)
        setupAdapter()

        return binding.root
    }

    private fun setupAdapter() {
        val adapter = ManageReportAdapter().apply {
            submitList(DummyData.dataDummyReport)
            onClickDetail = {
                Toast.makeText(requireContext(), "Detail", Toast.LENGTH_SHORT).show()
            }
        }
        binding.cardReport.recyclerViewContentTableLocation.let {
            it.adapter = adapter
            it.layoutManager = LinearLayoutManager(requireContext())
        }
    }

}