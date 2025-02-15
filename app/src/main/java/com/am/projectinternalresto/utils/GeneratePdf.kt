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
    // Show loading notification
    val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    val loadingNotificationId = 2

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        val channel = NotificationChannel(
            "pdf_channel",
            "PDF Notifications",
            NotificationManager.IMPORTANCE_DEFAULT
        )
        notificationManager.createNotificationChannel(channel)
    }

    val loadingNotification = NotificationCompat.Builder(context, "pdf_channel")
        .setSmallIcon(R.drawable.baseline_notifications_active_24)
        .setContentTitle("Generating PDF Report")
        .setContentText("Please wait while we generate your report...")
        .setProgress(0, 0, true)
        .setOngoing(true)
        .build()

    notificationManager.notify(loadingNotificationId, loadingNotification)

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
        val paymentMethodTotals = mutableMapOf<String, Int>()

        // Add table rows
        response.data?.forEach { order ->
            val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            val date = dateFormat.parse(order?.tanggalPemesanan ?: "")
            val formattedDate = date?.let { dateFormat.format(it) }

            val totalQty = order?.pesanan?.sumOf { it?.qty ?: 0 } ?: 0
            val totalPayment = order?.totalPaymentUser

            table.addCell(formattedDate)
            table.addCell(order?.nomorOrder ?: "")
            table.addCell(totalQty.toString())
            table.addCell("Rp ${NumberFormat.getNumberInstance(Locale("id", "ID")).format(0)}")
            table.addCell("Rp ${NumberFormat.getNumberInstance(Locale("id", "ID")).format(totalPayment)}")
            table.addCell(order?.statusPesanan ?: "")
            table.addCell(order?.metode ?: "")

            totalAmount += totalPayment ?: 0

            // Update payment method counts and totals
            val method = order?.metode ?: ""
            paymentMethodCounts[method] = (paymentMethodCounts[method] ?: 0) + 1
            paymentMethodTotals[method] = (paymentMethodTotals[method] ?: 0) + (totalPayment ?: 0)
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

        // Create summary table with three columns
        val summaryTable = Table(UnitValue.createPercentArray(floatArrayOf(30f, 35f, 35f)))
        summaryTable.width = UnitValue.createPercentValue(100f)

        // Add summary table headers
        summaryTable.addHeaderCell(Cell().add(Paragraph("Metode Pembayaran").setBold()))
        summaryTable.addHeaderCell(Cell().add(Paragraph("Jumlah Transaksi").setBold()))
        summaryTable.addHeaderCell(Cell().add(Paragraph("Total Pembayaran").setBold()))

        // Add payment method summary with totals
        paymentMethodCounts.forEach { (method, count) ->
            summaryTable.addCell(method)
            summaryTable.addCell(count.toString())
            summaryTable.addCell("Rp ${NumberFormat.getNumberInstance(Locale("id", "ID")).format(paymentMethodTotals[method])}")
        }

        document.add(summaryTable)
        document.close()

        // Create URI for PDF file
        val file = File(filePath)
        val uri = FileProvider.getUriForFile(context, "${context.packageName}.fileprovider", file)

        // Remove loading notification and show completion notification
        notificationManager.cancel(loadingNotificationId)
        sendNotification(context, "PDF Report Generated", "Your report is ready to view", uri)

        return uri
    } catch (e: Exception) {
        // Remove loading notification and show error notification
        notificationManager.cancel(loadingNotificationId)
        sendNotification(context, "Error", "Failed to generate PDF report", null)
        e.printStackTrace()
        return null
    }
}


private fun sendNotification(context: Context, title: String, message: String, pdfUri: Uri?) {
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