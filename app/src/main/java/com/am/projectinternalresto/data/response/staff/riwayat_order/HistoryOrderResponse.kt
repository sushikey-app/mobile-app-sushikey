package com.am.projectinternalresto.data.response.staff.riwayat_order

import com.am.projectinternalresto.data.response.staff.order.DataItemListOrder
import com.google.gson.annotations.SerializedName

data class HistoryOrderResponse(

    @field:SerializedName("data")
    val data: DataItemHistory? = null,

    @field:SerializedName("message")
    val message: String? = null,

    @field:SerializedName("status")
    val status: String? = null
)

data class DataItemHistory(

    @field:SerializedName("pesanan_hari_sebelumnya")
    val pesananHariSebelumnya: List<DataItemListOrder>? = null,

    @field:SerializedName("pesanan_hari_ini")
    val pesananHariIni: List<DataItemListOrder?>? = null
)
