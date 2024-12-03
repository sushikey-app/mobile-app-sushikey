package com.am.projectinternalresto.data.response.super_admin.report

import com.am.projectinternalresto.data.response.staff.order.ToppingItem
import com.google.gson.annotations.SerializedName

data class DetailReportOrderResponse(

	@field:SerializedName("data")
	val data: DataItemDetailReport? = null,

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("status")
	val status: String? = null
)

data class ItemDataReport(

	@field:SerializedName("note")
	val note: String? = null,

	@field:SerializedName("qty")
	val qty: Int? = null,

	@field:SerializedName("id")
	val id: String? = null,

	@field:SerializedName("menu")
	val menu: Menu? = null,

	@field:SerializedName("topping")
	val topping: List<ToppingItem?>? = null,

	@field:SerializedName("harga_pesanan")
	val hargaPesanan: Int? = null
)


data class DataItemDetailReport(

	@field:SerializedName("nama_pembeli")
	val namaPembeli: String? = null,

	@field:SerializedName("metode")
	val metode: String? = null,

	@field:SerializedName("uang_dibayarkan")
	val uangDibayarkan: Int? = null,

	@field:SerializedName("status_pembayaran")
	val statusPembayaran: String? = null,

	@field:SerializedName("nomor_order")
	val nomorOrder: String? = null,

	@field:SerializedName("total_harga")
	val totalHarga: Int? = null,

	@field:SerializedName("id")
	val id: String? = null,

	@field:SerializedName("lokasi_id")
	val lokasiId: String? = null,

	@field:SerializedName("type_pesanan")
	val typePesanan: String? = null,

	@field:SerializedName("pesanan")
	val pesanan: List<ItemDataReport?>? = null,

	@field:SerializedName("status_pesanan")
	val statusPesanan: String? = null
)
