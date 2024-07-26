package com.am.projectinternalresto.utils

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.navigation.NavController
import com.am.projectinternalresto.R

object Navigation {

    /*fungsi ini digunakan untuk menavigasi fragment dengan navigation*/
    fun navigateFragment(
        destination: Destination,
        navigationController: NavController,
        args: Bundle? = null
    ) {
        navigationController.let {
            when (destination) {
                /*Navigation from super admin*/
                Destination.MANAGE_ADMIN_TO_ADD_OR_UPDATE_ADMIN -> it.navigate(R.id.action_nav_super_admin_manage_admin_to_addOrUpdateAdmin)
                Destination.MANAGE_LOCATION_TO_ADD_OR_UPDATE_LOCATION -> it.navigate(R.id.action_nav_super_admin_location_to_addOrUpdateLocationFragment)
                Destination.MANAGE_ADMIN_TO_DETAIL_MANAGE_ADMIN -> it.navigate(R.id.action_nav_super_admin_manage_admin_to_detail_manage_admin)

                /*Navigation from admin*/
                Destination.MANAGE_MENU_TO_ADD_OR_UPDATE_MENU -> it.navigate(R.id.action_nav_admin_menu_to_addOrUpdateMenuFragment)
                Destination.MANAGE_STAFF_TO_ADD_OR_UPDATE_STAFF -> it.navigate(R.id.action_nav_admin_staff_to_addOrUpdateStaff)
                Destination.MANAGE_CATEGORY_TO_ADD_OR_UPDATE_CATEGORY -> it.navigate(
                    R.id.action_nav_admin_category_to_addOrUpdateCategory,
                    args
                )
            }
        }
    }

    /*extension for intent activity with bundle or not*/
    fun Activity.goToActivity(targetActivity: Activity, bundle: Bundle? = null) {
        val intent = Intent(this, targetActivity::class.java)
        if (bundle != null) {
            intent.putExtras(bundle)
        }
        startActivity(intent)
    }
}

enum class Destination {
    MANAGE_MENU_TO_ADD_OR_UPDATE_MENU,
    MANAGE_LOCATION_TO_ADD_OR_UPDATE_LOCATION,
    MANAGE_ADMIN_TO_ADD_OR_UPDATE_ADMIN,
    MANAGE_ADMIN_TO_DETAIL_MANAGE_ADMIN,
    MANAGE_CATEGORY_TO_ADD_OR_UPDATE_CATEGORY,
    MANAGE_STAFF_TO_ADD_OR_UPDATE_STAFF
}