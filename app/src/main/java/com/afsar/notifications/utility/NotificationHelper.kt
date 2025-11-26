package com.afsar.notifications.utility

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import com.afsar.notifications.MainActivity
import com.afsar.notifications.R

fun showNotification(context: Context, title: String, message: String) {
    val channelId = "titipan_channel"
    val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    // 1. Buat Channel (Wajib untuk Android O+)
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        val channel = NotificationChannel(
            channelId,
            "Status Request Titipan",
            NotificationManager.IMPORTANCE_DEFAULT
        )
        notificationManager.createNotificationChannel(channel)
    }

    // 2. Intent agar saat notif diklik, aplikasi terbuka
    val intent = Intent(context, MainActivity::class.java)
    val pendingIntent = PendingIntent.getActivity(
        context, 0, intent, PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
    )

    // (Saya sudah menghapus bagian BitmapFactory karena tidak dipakai lagi)

    // 3. Build Notifikasi
    val builder = NotificationCompat.Builder(context, channelId)
        // Ikon kecil di kiri (Wajib ada)
        .setSmallIcon(R.drawable.ic_stat_name)

        // --- BAGIAN INI SUDAH DIHAPUS ---
        // .setLargeIcon(...) -> Hilang, jadi tidak ada gambar di kanan.

        .setContentTitle(title)
        .setContentText(message)
        .setStyle(NotificationCompat.BigTextStyle().bigText(message))
        .setPriority(NotificationCompat.PRIORITY_DEFAULT)
        .setContentIntent(pendingIntent)
        .setAutoCancel(true)

    try {
        // ID statis (1) agar notifikasi menimpa yang lama (tidak numpuk)
        notificationManager.notify(1, builder.build())
    } catch (e: Exception) {
        e.printStackTrace()
    }
}