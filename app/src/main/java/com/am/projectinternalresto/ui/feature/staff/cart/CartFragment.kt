package com.am.projectinternalresto.ui.feature.staff.cart

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.am.projectinternalresto.data.dummy.DummyData
import com.am.projectinternalresto.databinding.FragmentCartBinding
import com.am.projectinternalresto.ui.adapter.cart.CartAdapter
import com.am.projectinternalresto.ui.adapter.cart.PaymentAdapter
import com.am.projectinternalresto.utils.UiHandler

class CartFragment : Fragment() {
    private var _binding: FragmentCartBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCartBinding.inflate(inflater, container, false)
        setupAdapterDataCart()

        return binding.root
    }

    private fun setupAdapterDataCart() {
        val adapter = CartAdapter().apply {
            submitList(DummyData.dataDummyCart)
            callbackOnClickDelete = {
                UiHandler.toastSuccessMessage(requireContext(), "Berhasil Hapus Data")
            }
            callbackOnClickPlus = {
                UiHandler.toastSuccessMessage(requireContext(), "Berhasil Menambah qty")
            }
            callbackOnclickMinus = {
                UiHandler.toastSuccessMessage(requireContext(), "Berhasil Mengurangi qty")
            }
        }

        val adapterPayment = PaymentAdapter()
        adapterPayment.submitList(DummyData.dataDummyCart)


        binding.cardDetailInformationOrderCart.recyclerViewItemOrder.let {
            it.adapter = adapter
            it.layoutManager = LinearLayoutManager(requireContext())
        }

        binding.cardPayment.recyclerViewItemPayment.let {
            it.adapter = adapterPayment
            it.layoutManager = LinearLayoutManager(requireContext())
        }
    }


}