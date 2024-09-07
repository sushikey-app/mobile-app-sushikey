package com.am.projectinternalresto.utils

import android.os.Bundle
import androidx.navigation.NavController
import com.am.projectinternalresto.R

object Navigation {

    /*function for all navigation in application*/
    fun navigateFragment(
        destination: Destination,
        navigationController: NavController,
        args: Bundle? = null
    ) {
        navigationController.let {
            when (destination) {
                /*Navigation from super admin*/
                Destination.MANAGE_ADMIN_TO_ADD_OR_UPDATE_ADMIN -> it.navigate(R.id.action_nav_super_admin_manage_admin_to_addAdminOrSuperAdminFragment, args)
                Destination.MANAGE_LOCATION_TO_ADD_OR_UPDATE_LOCATION -> it.navigate(R.id.action_nav_super_admin_location_to_addOrUpdateLocationFragment, args)

                /*Navigation from admin*/
                Destination.MANAGE_MENU_TO_ADD_OR_UPDATE_MENU -> it.navigate(
                    R.id.action_nav_admin_menu_to_addOrUpdateMenuFragment,
                    args
                )

                Destination.MANAGE_STAFF_TO_ADD_OR_UPDATE_STAFF -> it.navigate(
                    R.id.action_nav_admin_staff_to_addOrUpdateStaff,
                    args
                )

                Destination.MANAGE_CATEGORY_TO_ADD_OR_UPDATE_CATEGORY -> it.navigate(
                    R.id.action_nav_admin_category_to_addCategoryMenuFragment,
                    args
                )

                /*Navigation from staff*/
                Destination.ORDER_MENU_TO_CONFIRM_ORDER_AND_PAYMENT_METHOD -> it.navigate(
                    R.id.action_nav_staff_menu_to_confirmOrderAndPaymentMethodFragment,
                    args
                )
            }
        }
    }
}

enum class Destination {
    MANAGE_LOCATION_TO_ADD_OR_UPDATE_LOCATION,
    MANAGE_ADMIN_TO_ADD_OR_UPDATE_ADMIN,

    MANAGE_MENU_TO_ADD_OR_UPDATE_MENU,
    MANAGE_CATEGORY_TO_ADD_OR_UPDATE_CATEGORY,
    MANAGE_STAFF_TO_ADD_OR_UPDATE_STAFF,

    ORDER_MENU_TO_CONFIRM_ORDER_AND_PAYMENT_METHOD
}