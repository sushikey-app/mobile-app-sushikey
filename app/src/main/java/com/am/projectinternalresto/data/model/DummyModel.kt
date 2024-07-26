package com.am.projectinternalresto.data.model

sealed class DummyModel {
    data class DummyModelMenuFavorite(
        val nameMenu: String,
        val locationOutlet: String,
        val totalSales: String
    )

    data class DummyModelManageLocation(
        val no: Int,
        val restoName: String,
        val restoLocation: String,
        val numberTelephone: String
    )

    data class DummyModelSelectLocation(
        val dataLocation: DummyModelManageLocation,
        var isChecked: Boolean
    )

    data class DummyModelManageAdmin(
        val name: String,
        val username: String,
        val division: String,
        val location: String,
        val phoneNumber: String
    )

    data class DummyModelReport(
        val no: Int,
        val Date: String,
        val numberOrder: String,
        val location : String,
        val total: String
    )

    data class DummyModelCategory(
        val id: String,
        val name: String,
    )

    data class DummyModelMenu(
        val numberMenu: Int,
        val category: String,
        val nameProduct: String,
        val quota: Int,
        val price: String,
        val image: Int
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
}