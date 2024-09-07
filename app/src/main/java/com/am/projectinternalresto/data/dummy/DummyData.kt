package com.am.projectinternalresto.data.dummy

import com.am.projectinternalresto.R
import com.am.projectinternalresto.data.model.DummyModel
import com.am.projectinternalresto.utils.Formatter.formatCurrency

object DummyData {
    val dummyCardMenuFavorite = listOf(
        DummyModel.DummyModelMenuFavorite(
            "Chicken Sushi", "Surabaya", "200"
        ),
        DummyModel.DummyModelMenuFavorite(
            "Tuna Sushi", "Bali", "150"
        ),
        DummyModel.DummyModelMenuFavorite(
            "Squid Sushi", "Jogja", "100"
        ),
        DummyModel.DummyModelMenuFavorite(
            "Chicken Sushi", "Solo", "200"
        ),
        DummyModel.DummyModelMenuFavorite(
            "Tuna Sushi", "Semarang", "150"
        ),
    )

    val dataDummyReport = arrayListOf(
        DummyModel.DummyModelReport(1, "17 Feb 2024", "ORD_01", "Surabaya", formatCurrency(200000)),
        DummyModel.DummyModelReport(2, "19 Feb 2024", "ORD_02", "Jogja", formatCurrency(100000)),
        DummyModel.DummyModelReport(3, "20 Feb 2024", "ORD_03", "Semarang", formatCurrency(250000)),
    )

    val paymentOption = listOf(
        formatCurrency(5000),
        formatCurrency(10000),
        formatCurrency(20000),
        formatCurrency(50000),
        formatCurrency(100000)
    )

}
