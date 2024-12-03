package com.am.projectinternalresto.utils

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.content.FileProvider
import com.am.projectinternalresto.R
import com.am.projectinternalresto.data.response.super_admin.report.DataItemReport
import com.itextpdf.kernel.pdf.PdfDocument
import com.itextpdf.kernel.pdf.PdfWriter
import com.itextpdf.layout.Document
import com.itextpdf.layout.element.Cell
import com.itextpdf.layout.element.Paragraph
import com.itextpdf.layout.element.Table
import com.itextpdf.layout.property.TextAlignment
import com.itextpdf.layout.property.UnitValue
import java.io.File
import java.io.FileOutputStream
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.Locale

fun generatePDFReport(context: Context, data: List<DataItemReport>): Uri? {
    val fileName = "LaporanPesanan.pdf"
    val filePath = context.getExternalFilesDir(null)?.absolutePath + File.separator + fileName

    try {
        val writer = PdfWriter(FileOutputStream(filePath))
        val pdf = PdfDocument(writer)
        val document = Document(pdf)

        // Add title
        val title = Paragraph("LAPORAN PESANAN")
        title.setTextAlignment(TextAlignment.CENTER)
        title.setBold()
        document.add(title)

        // Create table
        val table = Table(UnitValue.createPercentArray(floatArrayOf(20f, 20f, 20f, 15f, 10f, 15f)))
        table.width = UnitValue.createPercentValue(100f)

        // Add table headers
        val headers = arrayOf(
            "Tanggal Pemesanan",
            "Nomor Order",
            "Menu",
            "Harga",
            "Qty",
            "Total Harga Pesanan"
        )
        headers.forEach { header ->
            table.addHeaderCell(Cell().add(Paragraph(header).setBold()))
        }

        var totalHargaSemuaPesanan = 0

        // Add table rows
        data.filter { it.typePesanan == "Offline" }.forEach { order ->
            val tanggalPemesanan = SimpleDateFormat(
                "yyyy-MM-dd",
                Locale.getDefault()
            ).parse(order.tanggalPemesanan.toString())
            val formattedDate =
                SimpleDateFormat("dd-MM-yyyy / HH:mm:ss", Locale.getDefault()).format(
                    tanggalPemesanan
                )

            order.pesanan?.forEach { item ->
                table.addCell(formattedDate)
                table.addCell(order.nomorOrder)
                table.addCell(item?.menu?.nama)
                table.addCell(
                    "Rp ${
                        NumberFormat.getNumberInstance(Locale("id", "ID")).format(item?.menu?.harga)
                    }"
                )
                table.addCell(item?.qty.toString())

                val totalHarga = item?.menu?.harga?.times(item.qty ?: 0)
                table.addCell(
                    "Rp ${
                        NumberFormat.getNumberInstance(Locale("id", "ID")).format(totalHarga)
                    }"
                )

                totalHargaSemuaPesanan += totalHarga ?: 0
            }
        }

        // Add total row
        table.addCell(
            Cell(1, 5).add(
                Paragraph("Total Harga Semua Pesanan").setTextAlignment(
                    TextAlignment.RIGHT
                ).setBold()
            )
        )
        table.addCell(
            Cell().add(
                Paragraph(
                    "Rp ${
                        NumberFormat.getNumberInstance(Locale("id", "ID"))
                            .format(totalHargaSemuaPesanan)
                    }"
                ).setBold()
            )
        )

        document.add(table)
        document.close()

        // Membuat Uri untuk file PDF
        val file = File(filePath)
        val uri = FileProvider.getUriForFile(context, "${context.packageName}.fileprovider", file)

        // Mengirim notifikasi bahwa PDF sudah jadi
        sendNotification(context, "PDF Report Generated", "Your report is ready to view", uri)

        return uri
    } catch (e: Exception) {
        e.printStackTrace()
        return null
    }
}

private fun sendNotification(context: Context, title: String, message: String, pdfUri: Uri) {
    val notificationManager =
        context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        val channel = NotificationChannel(
            "pdf_channel",
            "PDF Notifications",
            NotificationManager.IMPORTANCE_DEFAULT
        )
        notificationManager.createNotificationChannel(channel)
    }

    val intent = Intent(Intent.ACTION_VIEW).apply {
        setDataAndType(pdfUri, "application/pdf")
        flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_GRANT_READ_URI_PERMISSION
    }
    val pendingIntent = PendingIntent.getActivity(
        context,
        0,
        intent,
        PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
    )

    val notification = NotificationCompat.Builder(context, "pdf_channel")
        .setSmallIcon(R.drawable.baseline_notifications_active_24) // Ganti dengan icon yang sesuai
        .setContentTitle(title)
        .setContentText(message)
        .setContentIntent(pendingIntent)
        .setAutoCancel(true)
        .build()

    notificationManager.notify(1, notification)
}