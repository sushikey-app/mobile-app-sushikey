package com.am.projectinternalresto.data.response.super_admin.report

import com.google.gson.annotations.SerializedName

data class PrintReportResponse(

	@field:SerializedName("total_pesanan")
	val totalPesanan: TotalPesanan? = null,

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

data class GOJEK(

	@field:SerializedName("pesanan_dibatalkan")
	val pesananDibatalkan: Int? = null,

	@field:SerializedName("pesanan_selesai")
	val pesananSelesai: Int? = null
)

data class TotalPesanan(

	@field:SerializedName("TRANSFER")
	val tRANSFER: TRANSFER? = null,

	@field:SerializedName("GOJEK")
	val gOJEK: GOJEK? = null
)


data class TRANSFER(

	@field:SerializedName("pesanan_dibatalkan")
	val pesananDibatalkan: Int? = null,

	@field:SerializedName("pesanan_selesai")
	val pesananSelesai: Int? = null
)
