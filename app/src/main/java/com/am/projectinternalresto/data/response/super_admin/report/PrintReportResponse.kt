package com.am.projectinternalresto.data.response.super_admin.report

import com.google.gson.annotations.SerializedName

data class PrintReportResponse(

	@field:SerializedName("total_pesanan")
	val totalPesanan: List<TotalPesanan>? = null,

	@field:SerializedName("data")
	val data: List<DataItem?>? = null,

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("status")
	val status: String? = null
)

data class DataItem(


	@field:SerializedName("nama_pembeli")
	val namaPembeli: String? = null,

	@field:SerializedName("metode")
	val metode: String? = null,

	@field:SerializedName("tanggal_pemesanan")
	val tanggalPemesanan: String? = null,

	@field:SerializedName("total_pembayaran")
	val totalPaymentUser: Int? = null,

	@field:SerializedName("status_pembayaran")
	val statusPembayaran: String? = null,

	@field:SerializedName("lokasi")
	val lokasi: Lokasi? = null,

	@field:SerializedName("nomor_order")
	val nomorOrder: String? = null,

	@field:SerializedName("id")
	val id: String? = null,

	@field:SerializedName("pesanan")
	val pesanan: List<PesananItem?>? = null,

	@field:SerializedName("status_pesanan")
	val statusPesanan: String? = null
)

data class TotalPesanan(

	@field:SerializedName("TRANSFER")
	val tRANSFER: PaymentOption? = null,

	@field:SerializedName("QRIS")
	val qris: PaymentOption? = null,

	@field:SerializedName("GOJEK")
	val gojek: PaymentOption? = null,

	@field:SerializedName("Grab")
	val grab: PaymentOption? = null,

	@field:SerializedName("TUNAI")
	val tunai: PaymentOption? = null
)

data class PaymentOption(

	@field:SerializedName("total_pembayaran_pesanan")
	val totalPayment: Int? = null,

	@field:SerializedName("pesanan_dibatalkan")
	val pesananDibatalkan: Int? = null,

	@field:SerializedName("pesanan_selesai")
	val pesananSelesai: Int? = null
)
