package com.am.projectinternalresto.data.body_params

import com.am.projectinternalresto.data.response.admin.menu.ToppingItem

data class UpdateOrderRequest(
    val pesanan: List<UpdateOrderItem>
)

data class UpdateOrderItem(
    val id: String? = null,
    val menu_id: String,
    val qty: Int,
    val note: String,
    val topping: List<ToppingItem>
)
