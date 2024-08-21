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

    val dummyDataCardLocation = listOf(
        DummyModel.DummyModelManageLocation(
            1, "SushiKey", "Jakarta", "089080808123",
        ), DummyModel.DummyModelManageLocation(
            2, "SushiKey", "Surabaya", "089080808123",
        ), DummyModel.DummyModelManageLocation(
            3, "SushiKey", "Bali", "089080808123",
        )
    )

    val dummyDataSelectLocation = arrayListOf(
        DummyModel.DummyModelSelectLocation(
            dataLocation = DummyModel.DummyModelManageLocation(
                1, "SushiKey", "Bali", "089080808123",
            ), isChecked = false
        ),
        DummyModel.DummyModelSelectLocation(
            dataLocation = DummyModel.DummyModelManageLocation(
                1, "SushiKey", "Bali", "089080808123",
            ), isChecked = false
        ),
        DummyModel.DummyModelSelectLocation(
            dataLocation = DummyModel.DummyModelManageLocation(
                1, "SushiKey", "Bali", "089080808123",
            ), isChecked = false
        ),
        DummyModel.DummyModelSelectLocation(
            dataLocation = DummyModel.DummyModelManageLocation(
                1, "SushiKey", "Bali", "089080808123",
            ), isChecked = false
        ),
    )

    val dummyDataManageAdmin = arrayListOf(
        DummyModel.DummyModelManageAdmin(
            name = "Achmad",
            "achmad_01",
            "Admin",
            "Surabaya",
            "0878810280120"
        ),
        DummyModel.DummyModelManageAdmin(
            name = "nusa",
            "nusa_01",
            "Admin",
            "Gresik",
            "0878810280111"
        ),
        DummyModel.DummyModelManageAdmin(
            name = "Muchlas",
            "muchlas_02",
            "Super Admin",
            "Jogja",
            "087881028000"
        )
    )

    val dataDummyReport = arrayListOf(
        DummyModel.DummyModelReport(1, "17 Feb 2024", "ORD_01", "Surabaya", formatCurrency(200000)),
        DummyModel.DummyModelReport(2, "19 Feb 2024", "ORD_02", "Jogja", formatCurrency(100000)),
        DummyModel.DummyModelReport(3, "20 Feb 2024", "ORD_03", "Semarang", formatCurrency(250000)),
    )

    val dataDummyCategory = arrayListOf(
        DummyModel.DummyModelCategory("FD", "Food"),
        DummyModel.DummyModelCategory("DK", "Drink"),
    )

    val dataDummyMenu = arrayListOf(
        DummyModel.DummyModelMenu(
            1, "Makanan", "Sushi", 10, formatCurrency(200000), R.drawable.image_menu
        ),
        DummyModel.DummyModelMenu(
            2, "Minuman", "Lemon tea", 10, formatCurrency(200000), R.drawable.image_menu
        ),
    )

    val dataDummyCart = arrayListOf(
        DummyModel.DummyModelCart(
            R.drawable.image_menu,
            "Sushsi",
            "All",
            "tidak pedas",
            formatCurrency(200000),
            1,
            formatCurrency(200000),
        ),
        DummyModel.DummyModelCart(
            R.drawable.image_menu,
            "Sushsi",
            "saikoro",
            "pedas",
            formatCurrency(150000),
            1,
            formatCurrency(150000),
        ),
    )

}
