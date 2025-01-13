package com.am.projectinternalresto.data.response.staff.riwayat_order

import com.google.gson.annotations.SerializedName

data class DetailHistoryOrderResponse(

	@field:SerializedName("data")
	val data: DataItemDetailHistoryOrder? = null,

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("status")
	val status: String? = null
)

data class DataItemDetailHistoryOrder(

	@field:SerializedName("pembayaran")
	val pembayaran: Pembayaran? = null,

	@field:SerializedName("pesanan")
	val pesanan: List<PesananItem?>? = null
)

data class Pembayaran(

	@field:SerializedName("nama_pembeli")
	val namaPembeli: String? = null,

	@field:SerializedName("metode")
	val metode: String? = null,

	@field:SerializedName("uang_dibayarkan")
	val uangDibayarkan: Int? = null,

	@field:SerializedName("updated_at")
	val updatedAt: String? = null,

	@field:SerializedName("status_pembayaran")
	val statusPembayaran: String? = null,

	@field:SerializedName("nomor_order")
	val nomorOrder: String? = null,

	@field:SerializedName("created_at")
	val createdAt: String? = null,

	@field:SerializedName("total_harga")
	val totalHarga: Int? = null,

	@field:SerializedName("id")
	val id: String? = null,

	@field:SerializedName("lokasi_id")
	val lokasiId: String? = null,

	@field:SerializedName("type_pesanan")
	val typePesanan: String? = null,

	@field:SerializedName("status_pesanan")
	val statusPesanan: String? = null
)

data class PesananItem(

	@field:SerializedName("note")
	val note: String? = null,

	@field:SerializedName("user_id")
	val userId: String? = null,

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

data class ToppingItem(

	@field:SerializedName("nama")
	val nama: String? = null,

	@field:SerializedName("harga")
	val harga: Int? = null,


	@field:SerializedName("id")
	val id: String? = null,

)
