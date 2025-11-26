🔔 Notifications (Titip.in Demo)

Aplikasi demo Android sederhana yang dibuat menggunakan Kotlin dan Jetpack Compose. Proyek ini secara khusus berfokus pada implementasi fitur Notifikasi dan penanganan izin notifikasi untuk perangkat modern (Android 13/API 33+).

🚀 Fitur Utama

Pemuatan Data Lokal: Aplikasi memuat daftar tugas/titipan dari file JSON statis (titipin-data.json) yang disimpan di folder assets [cite: uploaded:mamat1815/notifications/Notifications-fa0f39099098de06e52437d3f8aab18352efeb79/app/src/main/assets/titipin-data.json, uploaded:mamat1815/notifications/Notifications-fa0f39099098de06e52437d3f8aab18352efeb79/app/src/main/java/com/afsar/notifications/data/local/TitipanRepository.kt].

Tampilan Daftar: Menampilkan data barang titipan (nama barang, pemesan, harga, dan catatan) dalam LazyColumn di TitipanScreen [cite: uploaded:mamat1815/notifications/Notifications-fa0f39099098de06e52437d3f8aab18352efeb79/app/src/main/java/com/afsar/notifications/TitipanScreen.kt].

Pemicu Notifikasi: Notifikasi akan dipicu secara otomatis segera setelah proses pemuatan data selesai [cite: uploaded:mamat1815/notifications/Notifications-fa0f39099098de06e52437d3f8aab18352efeb79/app/src/main/java/com/afsar/notifications/TitipanScreen.kt, uploaded:mamat1815/notifications/Notifications-fa0f39099098de06e52437d3f8aab18352efeb79/app/src/main/java/com/afsar/notifications/ui/viewmodel/TitipanViewModel.kt].

Notifikasi Adaptif (API 33+): Aplikasi meminta izin android.permission.POST_NOTIFICATIONS secara dinamis saat dijalankan di Android 13 (TIRAMISU) atau versi yang lebih baru [cite: uploaded:mamat1815/notifications/Notifications-fa0f39099098de06e52437d3f8aab18352efeb79/app/src/main/AndroidManifest.xml, uploaded:mamat1815/notifications/Notifications-fa0f39099098de06e52437d3f8aab18352efeb79/app/src/main/java/com/afsar/notifications/TitipanScreen.kt].

Pesan Notifikasi Acak: Pesan notifikasi yang ditampilkan dipilih secara acak dari salah satu item data yang berhasil dimuat [cite: uploaded:mamat1815/notifications/Notifications-fa0f39099098de06e52437d3f8aab18352efeb79/app/src/main/java/com/afsar/notifications/ui/viewmodel/TitipanViewModel.kt].

📸 Tangkapan Layar

Layar Utama

![https://raw.githubusercontent.com/mamat1815/Notifications/refs/heads/master/layarutama.jpeg]

(Catatan: Ganti placeholder di atas dengan tangkapan layar asli aplikasi Anda.)

💾 Mekanisme Notifikasi & Data

Proyek ini menggunakan kombinasi Coroutines, Flow, dan Notification Manager bawaan Android.

1. Pemuatan Data Asinkronus

Repository: TitipanRepository bertanggung jawab untuk meniru operasi I/O yang memakan waktu (menggunakan Thread.sleep(2000)) dan memuat data JSON menjadi daftar objek Titipan menggunakan Google Gson [cite: uploaded:mamat1815/notifications/Notifications-fa0f39099098de06e52437d3f8aab18352efeb79/app/build.gradle.kts, uploaded:mamat1815/notifications/Notifications-fa0f39099098de06e52437d3f8aab18352efeb79/app/src/main/java/com/afsar/notifications/data/local/TitipanRepository.kt].

ViewModel: TitipanViewModel menggunakan viewModelScope.launch untuk memanggil getTitipanData() dari Repository secara aman di background thread [cite: uploaded:mamat1815/notifications/Notifications-fa0f39099098de06e52437d3f8aab18352efeb79/app/src/main/java/com/afsar/notifications/ui/viewmodel/TitipanViewModel.kt].

2. Pemicuan Notifikasi

Flag Reaktif: ViewModel memiliki _processFinished: MutableStateFlow<Boolean> [cite: uploaded:mamat1815/notifications/Notifications-fa0f39099098de06e52437d3f8aab18352efeb79/app/src/main/java/com/afsar/notifications/ui/viewmodel/TitipanViewModel.kt]. Setelah data selesai dimuat, nilainya diubah menjadi true.

Effect di UI: Di TitipanScreen.kt, LaunchedEffect mengamati processFinished. Ketika nilainya menjadi true, ia menjalankan logika pengecekan izin notifikasi dan memanggil fungsi showNotification [cite: uploaded:mamat1815/notifications/Notifications-fa0f39099098de06e52437d3f8aab18352efeb79/app/src/main/java/com/afsar/notifications/TitipanScreen.kt].

Helper Notifikasi: Fungsi showNotification(context, message) membuat NotificationChannel (titipan_channel) dan membangun notifikasi menggunakan NotificationCompat.Builder [cite: uploaded:mamat1815/notifications/Notifications-fa0f39099098de06e52437d3f8aab18352efeb79/app/src/main/java/com/afsar/notifications/utility/NotificationHelper.kt].

🛠️ Tumpukan Teknologi

Bahasa: Kotlin [cite: uploaded:mamat1815/notifications/Notifications-fa0f39099098de06e52437d3f8aab18352efeb79/app/build.gradle.kts]

UI: Jetpack Compose (Material 3) [cite: uploaded:mamat1815/notifications/Notifications-fa0f39099098de06e52437d3f8aab18352efeb79/app/build.gradle.kts]

Asinkronus: Kotlin Coroutines & Flow [cite: uploaded:mamat1815/notifications/Notifications-fa0f39099098de06e52437d3f8aab18352efeb79/app/src/main/java/com/afsar/notifications/TitipanScreen.kt, uploaded:mamat1815/notifications/Notifications-fa0f39099098de06e52437d3f8aab18352efeb79/app/src/main/java/com/afsar/notifications/ui/viewmodel/TitipanViewModel.kt]

Data Parsing: Google Gson (com.google.code.gson) [cite: uploaded:mamat1815/notifications/Notifications-fa0f39099098de06e52437d3f8aab18352efeb79/app/build.gradle.kts]

📐 Arsitektur Kode (MVVM)

Aplikasi ini menggunakan pola desain Model-View-ViewModel (MVVM):

View (UI): TitipanScreen.kt [cite: uploaded:mamat1815/notifications/Notifications-fa0f39099098de06e52437d3f8aab18352efeb79/app/src/main/java/com/afsar/notifications/TitipanScreen.kt]

ViewModel: TitipanViewModel.kt [cite: uploaded:mamat1815/notifications/Notifications-fa0f39099098de06e52437d3f8aab18352efeb79/app/src/main/java/com/afsar/notifications/ui/viewmodel/TitipanViewModel.kt]

Model (Repository & Data): TitipanRepository.kt dan DataTitip.kt [cite: uploaded:mamat1815/notifications/Notifications-fa0f39099098de06e52437d3f8aab18352efeb79/app/src/main/java/com/afsar/notifications/data/local/TitipanRepository.kt, uploaded:mamat1815/notifications/Notifications-fa0f39099098de06e52437d3f8aab18352efeb79/app/src/main/java/com/afsar/notifications/data/model/DataTitip.kt]

🏃 Cara Menjalankan

Buka Proyek: Impor proyek ke Android Studio.

Sinkronisasi Gradle: Tunggu hingga Gradle selesai mengunduh dependensi.

Jalankan: Jalankan aplikasi pada emulator atau perangkat.

Uji Notifikasi: Klik tombol "Ambil Data Titipan". Aplikasi akan meminta izin notifikasi (di Android 13+) lalu menampilkan notifikasi setelah data dimuat.
