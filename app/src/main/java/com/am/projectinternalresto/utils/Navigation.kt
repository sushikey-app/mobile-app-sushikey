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
                Destination.MANAGE_ADMIN_TO_ADD_OR_UPDATE_ADMIN -> it.navigate(
                    R.id.action_nav_super_admin_manage_admin_to_addAdminOrSuperAdminFragment,
                    args
                )

                Destination.MANAGE_LOCATION_TO_ADD_OR_UPDATE_LOCATION -> it.navigate(R.id.action_nav_super_admin_location_to_addOrUpdateLocationFragment, args)

                Destination.MANAGE_REPORT_TO_CANCEL_ORDER -> it.navigate(
                    R.id.action_nav_super_admin_report_to_cancelOrderFragment,
                    args
                )
                Destination.CANCEL_ORDER_TO_DETAIL_ORDER -> it.navigate(R.id.action_cancelOrderFragment_to_detailHistoryFragment2, args)

                /*Navigation from admin*/
                Destination.MANAGE_MENU_TO_ADD_OR_UPDATE_MENU -> it.navigate(
                    R.id.action_nav_admin_menu_to_addOrUpdateMenuFragment,
                    args
                )

                Destination.MANAGE_REPORT_TO_DETAIL_REPORT -> it.navigate(
                    R.id.action_nav_super_admin_report_to_detailHistoryFragment2,
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

                Destination.MANAGE_REPORT_ADMIN_TO_DETAIL_ORDER -> it.navigate(R.id.action_nav_admin_report_to_detailHistoryFragment3)

                /*Navigation from staff*/
                Destination.ORDER_MENU_TO_CONFIRM_ORDER_AND_PAYMENT_METHOD -> it.navigate(
                    R.id.action_nav_staff_menu_to_confirmOrderAndPaymentMethodFragment,
                    args
                )

                Destination.ORDER_TO_DETAIL_ORDER -> it.navigate(
                    R.id.action_nav_staff_order_to_detailOrderFragment,
                    args
                )

                Destination.ORDER_TO_ORDER_MENU -> it.navigate(
                    R.id.action_nav_staff_order_to_nav_staff_menu,
                    args
                )

                Destination.ORDER_TO_CONFIRM_ORDER_AND_PAYMENT_METHOD -> it.navigate(
                    R.id.action_nav_staff_order_to_confirmOrderAndPaymentMethodFragment,
                    args
                )

                Destination.HISTORY_ORDER_TO_DETAIL_HISTORY_ORDER -> it.navigate(
                    R.id.action_nav_staff_order_history_to_detailHistoryFragment,
                    args
                )
            }
        }
    }
}

enum class Destination {
    MANAGE_LOCATION_TO_ADD_OR_UPDATE_LOCATION,
    MANAGE_ADMIN_TO_ADD_OR_UPDATE_ADMIN,
    MANAGE_REPORT_TO_CANCEL_ORDER,
    MANAGE_REPORT_TO_DETAIL_REPORT,
    CANCEL_ORDER_TO_DETAIL_ORDER,

    MANAGE_MENU_TO_ADD_OR_UPDATE_MENU,
    MANAGE_CATEGORY_TO_ADD_OR_UPDATE_CATEGORY,
    MANAGE_STAFF_TO_ADD_OR_UPDATE_STAFF,
    MANAGE_REPORT_ADMIN_TO_DETAIL_ORDER,

    ORDER_MENU_TO_CONFIRM_ORDER_AND_PAYMENT_METHOD,
    ORDER_TO_ORDER_MENU,
    ORDER_TO_CONFIRM_ORDER_AND_PAYMENT_METHOD,
    ORDER_TO_DETAIL_ORDER,
    HISTORY_ORDER_TO_DETAIL_HISTORY_ORDER,
}