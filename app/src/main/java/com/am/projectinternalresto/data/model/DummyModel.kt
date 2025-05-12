package com.am.projectinternalresto.data.model

import android.os.Parcelable
import com.am.projectinternalresto.data.response.admin.menu.DataItemMenu
import com.am.projectinternalresto.data.response.admin.menu.ToppingItem
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.RawValue
import java.util.UUID

sealed class DummyModel {
    data class DummyModelMenuFavorite(
        val nameMenu: String,
        val locationOutlet: String,
        val totalSales: String
    )


    data class DummyModelReport(
        val no: Int,
        val Date: String,
        val numberOrder: String,
        val location : String,
        val total: String
    )

    data class DummyModelCart(
        val image: Int,
        val name: String,
        val topping: String,
        val note: String,
        val price: String,
        val qty: Int,
        val total: String,
    )

    data class CartItem(
        val id: String = UUID.randomUUID().toString(),
        val idOrder: String? = null,
        val menuItem: DataItemMenu,
        var qty: Int,
        val selectedToppings: List<ToppingItem?> = emptyList(),
        val note: String = ""
    )

    @Parcelize
    data class OrderSummary(
        val orderId: String? = null,
        val listCartItems: @RawValue List<CartItem>,
        val totalPurchase: Int,
        val totalDisc: Int
    ) : Parcelable
}