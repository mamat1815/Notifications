# 🔔 Notifications (Titip.in Demo)

Aplikasi demo Android sederhana yang dibangun menggunakan Kotlin dan Jetpack Compose untuk mendemonstrasikan pemuatan data asinkronus (dari aset lokal) dan memicu **Notifikasi** kepada pengguna, termasuk penanganan izin notifikasi (`POST_NOTIFICATIONS`) untuk Android 13 (API 33) ke atas.

## ✨ Fitur Utama

* **Pemuatan Data:** Memuat daftar barang titipan dari file aset JSON (`titipin-data.json`).
* **Notifikasi Acak:** Setelah data berhasil dimuat, notifikasi akan ditampilkan secara otomatis, mengingatkan pengguna tentang salah satu barang titipan secara acak.
* **Izin Notifikasi:** Menangani permintaan izin `POST_NOTIFICATIONS` untuk perangkat yang menjalankan Android 13 (TIRAMISU) dan yang lebih baru.
* **UI Responsif:** Antarmuka pengguna dibangun sepenuhnya menggunakan Jetpack Compose.

## 🛠️ Tumpukan Teknologi

* **Bahasa:** Kotlin
* **UI:** Jetpack Compose
* **Arsitektur:** MVVM (menggunakan `ViewModel`, `Repository`)
* **Dependensi:** Google Gson (untuk parsing JSON)

## 🚀 Cara Menjalankan

1.  **Buka Proyek:** Impor proyek ke Android Studio.
2.  **Sinkronisasi Gradle:** Biarkan Gradle menyelesaikan sinkronisasi dependensi.
3.  **Jalankan:** Jalankan aplikasi pada emulator atau perangkat Android (disarankan API 33 atau lebih tinggi untuk menguji izin notifikasi).
4.  **Interaksi:** Klik tombol **"Ambil Data Titipan"** di layar utama untuk memuat data dan memicu notifikasi.

---

### Struktur Data (Model)

Data yang digunakan dalam aplikasi dimodelkan oleh kelas `Titipan`:

```kotlin
data class Titipan(
    val id: Int,
    val nama_barang: String,
    val pemesan: String,
    val estimasi_harga: Int,
    val catatan: String
)
