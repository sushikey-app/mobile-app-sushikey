package com.am.projectinternalresto.ui.feature.staff.order_menu

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.am.projectinternalresto.data.body_params.OrderItemRequest
import com.am.projectinternalresto.data.body_params.OrderRequest
import com.am.projectinternalresto.data.body_params.SaveOrderRequest
import com.am.projectinternalresto.data.body_params.ToppingItemRequest
import com.am.projectinternalresto.data.model.DummyModel
import com.am.projectinternalresto.data.response.admin.menu.DataItemMenu
import com.am.projectinternalresto.data.response.admin.menu.ToppingItem
import com.am.projectinternalresto.service.source.OrderRepository

class ManageOrderMenuViewModel(private val repository: OrderRepository) : ViewModel() {
    private val _cartItems = MutableLiveData<List<DummyModel.CartItem>?>()
    val cartItems: MutableLiveData<List<DummyModel.CartItem>?> = _cartItems
    private val _totalPurchase = MediatorLiveData<Int>().apply {
        addSource(_cartItems) { items ->
            value = calculateTotalPurchase(items)
        }
    }
    val totalPurchase: LiveData<Int> = _totalPurchase
    private var _orderType = MutableLiveData("Offline")
    val orderType: LiveData<String> = _orderType

    fun getSalesData(token: String) = repository.getDataSales(token)
    fun getDataMenuOrder(token: String) = repository.getMenuOrder(token)
    fun saveDataOrder(token: String) =
        repository.saveOrder(token, createSaveOrderRequest())

    fun orderMenu(token: String, orderRequest: OrderRequest) =
        repository.orderMenu(token, orderRequest)

    fun listOrder(token: String, paidStatus: String) = repository.listOrder(token, paidStatus)

    fun addToCartWithToppingsAndNote(
        menuItem: DataItemMenu, selectedToppings: List<ToppingItem>?, note: String
    ) {
        val currentList = _cartItems.value?.toMutableList() ?: mutableListOf()
        val existingItem = currentList.find {
            it.menuItem.idMenu == menuItem.idMenu && it.selectedToppings == selectedToppings && it.note == note
        }

        if (existingItem != null) {
            val index = currentList.indexOf(existingItem)
            currentList[index] = existingItem.copy(qty = existingItem.qty + 1)
        } else {
            currentList.add(
                DummyModel.CartItem(
                    menuItem = menuItem,
                    qty = 1,
                    selectedToppings = selectedToppings ?: emptyList(),
                    note = note
                )
            )
        }
        _cartItems.value = currentList
    }

    fun updateQuantity(itemId: String, increment: Boolean) {
        val currentList = _cartItems.value?.toMutableList() ?: return
        val index = currentList.indexOfFirst { it.menuItem.idMenu.toString() == itemId }
        if (index != -1) {
            val item = currentList[index]
            val newQty = if (increment) item.qty + 1 else item.qty - 1

            if (newQty > 0) {
                currentList[index] = item.copy(qty = newQty)
                _cartItems.value = currentList
            } else {
                currentList.removeAt(index)
                _cartItems.value = currentList
            }
        }
    }

    private fun calculateTotalPurchase(items: List<DummyModel.CartItem>?): Int {
        return items?.sumOf { cartItem ->
            val basePrice = cartItem.menuItem.price ?: 0
            val toppingPrice = cartItem.selectedToppings.sumOf { it.harga ?: 0 }
            (basePrice + toppingPrice) * cartItem.qty
        } ?: 0
    }

    private fun createSaveOrderRequest(): SaveOrderRequest {
        val itemOrder = _cartItems.value?.map { items ->
            OrderItemRequest(menuId = items.menuItem.idMenu.toString(),
                qty = items.qty,
                note = items.note,
                topping = items.selectedToppings.map { topping -> ToppingItemRequest(topping.id.toString()) })
        } ?: emptyList()

        return SaveOrderRequest(
            typeOrder = orderType.value ?: "Offline", order = itemOrder
        )
    }

    fun setTypeOrder(type: String) {
        _orderType.value = type
    }

    fun clearCart() {
        _cartItems.value = emptyList()
    }

}