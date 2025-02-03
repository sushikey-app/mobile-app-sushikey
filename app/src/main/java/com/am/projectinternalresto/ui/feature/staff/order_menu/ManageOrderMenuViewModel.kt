package com.am.projectinternalresto.ui.feature.staff.order_menu

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.am.projectinternalresto.data.body_params.OrderItemRequest
import com.am.projectinternalresto.data.body_params.OrderRequest
import com.am.projectinternalresto.data.body_params.PaymentRequest
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

//    // Tambahkan LiveData untuk menyimpan stok item
//    private val _itemStock = MutableLiveData<Map<String, Int>>()
//    val itemStock: LiveData<Map<String, Int>> = _itemStock

    fun getDataSales(token: String) = repository.getDataSales(token)
    fun getDataSalesAdmin(token: String) = repository.getDataSalesAdmin(token)
    fun getDataMenuOrder(token: String) = repository.getAllDataOrder(token)
    fun searchMenuOrder(token: String, keyword: String) =
        repository.searchDataMenuOrder(token, keyword)

    fun getDetailOrder(token: String, idOrder: String) = repository.getDetailsOrder(token, idOrder)
    fun saveDataOrder(token: String, nameCustomer: String) =
        repository.saveOrder(token, createSaveOrderRequest(nameCustomer))


    fun payFromOrderContinuation(token: String, orderRequest: OrderRequest) =
        repository.payFromOrderContinuation(token, orderRequest)

    fun listOrder(token: String, paidStatus: String) = repository.listOrder(token, paidStatus)
    fun changeStatusOrder(token: String, idOrder: String) =
        repository.changeStatusOrder(token, idOrder)

    fun paymentOrder(token: String, idOrder: String, body: PaymentRequest) =
        repository.paymentOrder(token, idOrder, body)

    fun initializeCartWithExistingOrder(orderSummary: DummyModel.OrderSummary) {
        _cartItems.value = orderSummary.listCartItems
    }

    // Fungsi untuk memperbarui stok dari backend
//    fun updateItemStock(menuId: String, stock: Int) {
//        val currentStock = _itemStock.value?.toMutableMap() ?: mutableMapOf()
//        currentStock[menuId] = stock
//        _itemStock.value = currentStock
//    }

    // Modifikasi fungsi addToCartWithToppingsAndNote
    fun addToCartWithToppingsAndNote(
        menuItem: DataItemMenu,
        selectedToppings: List<ToppingItem>?,
        note: String
    ): Boolean {
//        val currentStock = _itemStock.value?.get(menuItem.idMenu) ?: 0
//        if (currentStock <= 0) {
//            // Item habis stok
//            return false
//        }

        val currentList = _cartItems.value?.toMutableList() ?: mutableListOf()
        val existingItem = currentList.find {
            it.menuItem.idMenu == menuItem.idMenu && it.selectedToppings == selectedToppings && it.note == note
        }

        if (existingItem != null) {
//            if (existingItem.qty >= currentStock) {
//                // Tidak bisa menambah lagi karena melebihi stok
//                return false
//            }
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

        // Kurangi stok
//        updateItemStock(menuItem.idMenu.toString(), currentStock - 1)

        return true
    }

    fun updateQuantity(itemId: String, increment: Boolean): Boolean {
        val currentList = _cartItems.value?.toMutableList() ?: return false
        val index = currentList.indexOfFirst { it.id == itemId }
        if (index != -1) {
            val item = currentList[index]
//            val currentStock = _itemStock.value?.get(item.menuItem.idMenu) ?: 0
            val newQty = if (increment) item.qty + 1 else item.qty - 1

//            if (increment && item.qty >= currentStock) {
//                // Tidak bisa menambah lagi karena melebihi stok
//                return false
//            }

            if (newQty > 0) {
                currentList[index] = item.copy(qty = newQty)
                _cartItems.value = currentList
                // Update stok
//                updateItemStock(
//                    item.menuItem.idMenu.toString(),
//                    if (increment) currentStock - 1 else currentStock + 1
//                )
            } else {
                currentList.removeAt(index)
                _cartItems.value = currentList
                // Kembalikan stok
//                updateItemStock(item.menuItem.idMenu.toString(), currentStock + 1)
            }
            return true
        }
        return false
    }

    private fun calculateTotalPurchase(items: List<DummyModel.CartItem>?): Int {
        return items?.sumOf { cartItem ->
            val basePrice = cartItem.menuItem.price ?: 0
            val toppingPrice = cartItem.selectedToppings.sumOf { it.harga ?: 0 }
            (basePrice + toppingPrice) * cartItem.qty
        } ?: 0
    }

    private fun createSaveOrderRequest(nameCustomer: String): SaveOrderRequest {
        val itemOrder = _cartItems.value?.map { items ->
            OrderItemRequest(menuId = items.menuItem.idMenu.toString(),
                qty = items.qty,
                note = items.note,
                topping = items.selectedToppings.map { topping -> ToppingItemRequest(topping.id.toString()) })
        } ?: emptyList()

        return SaveOrderRequest(
            nameCustomer = nameCustomer,
            order = itemOrder
        )
    }

    fun clearCart() {
        _cartItems.value = emptyList()
    }

    fun getCancelOrder(token: String) = repository.getCancelOrder(token)
    fun cancelOrder(token: String, idOrder: String) = repository.cancelOrder(token, idOrder)

    fun confirmCancelOrder(token: String, idOrder: String, statusCancel: String, reason: String?) =
        repository.confirmCancelOrder(token, idOrder, statusCancel, reason)
}