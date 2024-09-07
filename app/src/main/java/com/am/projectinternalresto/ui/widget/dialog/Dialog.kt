package com.am.projectinternalresto.ui.widget.dialog

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.am.projectinternalresto.R
import com.am.projectinternalresto.data.response.admin.menu.DataItemMenu
import com.am.projectinternalresto.data.response.admin.menu.ToppingItem
import com.am.projectinternalresto.ui.adapter.manage_location.SelectToppingAdapter
import com.am.projectinternalresto.ui.feature.auth.LoginActivity
import com.am.projectinternalresto.ui.feature.staff.order_menu.ManageOrderMenuViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.textfield.TextInputEditText


fun showAlertLogout(context: Context) {
    val alertDialog = MaterialAlertDialogBuilder(context)
    alertDialog.apply {
        setTitle(R.string.title_nav_logout)
        setMessage(context.getString(R.string.confitm_exit_app))
        setPositiveButton(context.getString(R.string.yes)) { _, _ ->
            val targetActivity = Intent(context.applicationContext, LoginActivity::class.java)
            context.startActivity(targetActivity)
        }
        setNegativeButton(context.getString(R.string.cancel)) { dialog, _ ->
            dialog.dismiss()
        }
    }
    alertDialog.create()
    alertDialog.show()
}

fun showAlertAddToCart(
    context: Context, viewModel: ManageOrderMenuViewModel, menuItem: DataItemMenu
) {
    val builder = MaterialAlertDialogBuilder(context).create()
    val view =
        LayoutInflater.from(context).inflate(R.layout.custom_layout_dialog_topping_and_note, null)

    val buttonSave = view.findViewById(R.id.buttonSaveDialogAddToCart) as Button
    val recyclerView = view.findViewById(R.id.recyclerViewTopping) as RecyclerView
    val buttonClose = view.findViewById(R.id.buttonCloseDialog) as ImageView
    val edtNote = view.findViewById(R.id.edtNote) as TextInputEditText
    val txtIsEmpty = view.findViewById(R.id.textNoResultData) as TextView

    builder.setView(view)
    val toppingList = menuItem.topping?.map { topping ->
        ToppingItem(topping.nama, topping.id.toString(), topping.harga)
    }
    val adapter = SelectToppingAdapter().apply { submitList(toppingList) }
    Log.e("Check", "data : $toppingList")
    if (toppingList.isNullOrEmpty()) txtIsEmpty.visibility = View.VISIBLE
    recyclerView.layoutManager = GridLayoutManager(context, 2)
    recyclerView.adapter = adapter

    buttonClose.setOnClickListener {
        builder.dismiss()
    }
    buttonSave.setOnClickListener {
        val selectedToppings = toppingList?.filter { it.isSelected }
        val note = edtNote.text.toString()

        viewModel.addToCartWithToppingsAndNote(menuItem, selectedToppings, note)
        Log.e("Check", "ItemMenu : $menuItem, topping : $toppingList, note $note")
        builder.dismiss()
    }
    builder.setCanceledOnTouchOutside(false)
    builder.show()
}
