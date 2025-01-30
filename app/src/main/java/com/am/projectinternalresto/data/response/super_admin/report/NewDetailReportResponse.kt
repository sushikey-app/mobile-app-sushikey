package com.am.projectinternalresto.data.response.super_admin.report

import com.am.projectinternalresto.data.response.staff.order.ToppingItem
import com.google.gson.annotations.SerializedName

data class NewDetailReportResponse(

	@field:SerializedName("data")
	val data: DataItemNewReport? = null,

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("status")
	val status: String? = null
)

data class DataItemNewReport(

	@field:SerializedName("pembayaran")
	val pembayaran: PaymentDetailReport? = null,

	@field:SerializedName("nama_kasir")
	val namaKasir: String? = null
)

data class PaymentDetailReport(

	@field:SerializedName("nama_pembeli")
	val namaPembeli: String? = null,

	@field:SerializedName("uang_dibayarkan")
	val uangDibayarkan: Int? = null,

	@field:SerializedName("nomor_order")
	val nomorOrder: String? = null,

	@field:SerializedName("created_at")
	val createdAt: String? = null,

	@field:SerializedName("total_harga")
	val totalHarga: Int? = null,

	@field:SerializedName("lokasi_id")
	val lokasiId: String? = null,

	@field:SerializedName("pesanan")
	val pesanan: List<OrderItemsDetailReport?>? = null,

	@field:SerializedName("status_pesanan")
	val statusPesanan: String? = null,

	@field:SerializedName("metode")
	val metode: String? = null,

	@field:SerializedName("updated_at")
	val updatedAt: String? = null,

	@field:SerializedName("user_id")
	val userId: String? = null,

	@field:SerializedName("status_pembayaran")
	val statusPembayaran: String? = null,

	@field:SerializedName("id")
	val id: String? = null
)

data class OrderItemsDetailReport(

	@field:SerializedName("note")
	val note: Any? = null,

	@field:SerializedName("updated_at")
	val updatedAt: String? = null,

	@field:SerializedName("qty")
	val qty: Int? = null,

	@field:SerializedName("created_at")
	val createdAt: String? = null,

	@field:SerializedName("id")
	val id: String? = null,

	@field:SerializedName("topping")
	val topping: List<ToppingItem?>? = null,

	@field:SerializedName("harga_pesanan")
	val hargaPesanan: Int? = null,

	@field:SerializedName("menu_id")
	val menuId: String? = null
)
