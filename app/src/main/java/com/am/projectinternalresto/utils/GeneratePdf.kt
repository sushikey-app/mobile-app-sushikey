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
import com.am.projectinternalresto.data.response.super_admin.report.PrintReportResponse
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

fun generatePDFReport(context: Context, response: PrintReportResponse): Uri? {
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
        document.add(Paragraph("\n"))

        // Create main table
        val table = Table(UnitValue.createPercentArray(floatArrayOf(15f, 15f, 10f, 15f, 20f, 15f, 10f)))
        table.width = UnitValue.createPercentValue(100f)

        // Add table headers
        val headers = arrayOf(
            "Tanggal",
            "No Order",
            "Qty",
            "Potongan",
            "Total Pembayaran",
            "Status",
            "Pembayaran"
        )
        headers.forEach { header ->
            table.addHeaderCell(Cell().add(Paragraph(header).setBold()))
        }

        // Track totals for summary
        var totalAmount = 0
        val paymentMethodCounts = mutableMapOf<String, Int>()

        // Add table rows
        response.data?.forEach { order ->
            val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            val date = dateFormat.parse(order?.tanggalPemesanan ?: "")
            val formattedDate = dateFormat.format(date)

            val totalQty = order?.pesanan?.sumOf { it?.qty ?: 0 } ?: 0
            val totalPayment = order?.pesanan?.sumOf { (it?.menu?.harga ?: 0) * (it?.qty ?: 0) } ?: 0

            table.addCell(formattedDate)
            table.addCell(order?.nomorOrder ?: "")
            table.addCell(totalQty.toString())
            table.addCell("Rp ${NumberFormat.getNumberInstance(Locale("id", "ID")).format(0)}")
            table.addCell("Rp ${NumberFormat.getNumberInstance(Locale("id", "ID")).format(totalPayment)}")
            table.addCell(order?.statusPesanan ?: "")
            table.addCell(order?.metode ?: "")

            totalAmount += totalPayment
            paymentMethodCounts[order?.metode ?: ""] = (paymentMethodCounts[order?.metode] ?: 0) + 1
        }

        // Add total row
        table.addCell(
            Cell(1, 6).add(
                Paragraph("Total").setTextAlignment(TextAlignment.RIGHT).setBold()
            )
        )
        table.addCell(
            Cell().add(
                Paragraph(
                    "Rp ${NumberFormat.getNumberInstance(Locale("id", "ID")).format(totalAmount)}"
                ).setBold()
            )
        )

        document.add(table)
        document.add(Paragraph("\n"))

        // Add summary section
        val summaryTitle = Paragraph("RANGKUMAN")
        summaryTitle.setBold()
        document.add(summaryTitle)

        val summaryTable = Table(UnitValue.createPercentArray(floatArrayOf(30f, 70f)))
        summaryTable.width = UnitValue.createPercentValue(50f)

        // Add payment method summary
        paymentMethodCounts.forEach { (method, count) ->
            summaryTable.addCell(method)
            summaryTable.addCell(count.toString())
        }

        document.add(summaryTable)
        document.close()

        // Create URI for PDF file
        val file = File(filePath)
        val uri = FileProvider.getUriForFile(context, "${context.packageName}.fileprovider", file)

        // Send notification
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