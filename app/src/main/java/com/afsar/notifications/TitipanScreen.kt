package com.afsar.notifications

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import com.afsar.notifications.data.model.Titipan
import com.afsar.notifications.ui.viewmodel.TitipanViewModel
import com.afsar.notifications.utility.showNotification
import java.text.NumberFormat
import java.util.Locale

//private val Icons.Filled.ShoppingCart: ImageVector

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TitipanScreen(viewModel: TitipanViewModel = viewModel()) {
    val titipanList by viewModel.titipanList.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val processFinished by viewModel.processFinished.collectAsState()
    val notifMessage by viewModel.notificationMessage.collectAsState()

    val context = LocalContext.current
    val notifTitle = "Update Titipan"

    // Launcher Permission (Android 13+)
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { isGranted ->
            if (isGranted) showNotification(context, notifTitle, notifMessage)
        }
    )

    // Logika Notifikasi
    LaunchedEffect(processFinished) {
        if (processFinished) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                if (ContextCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED) {
                    showNotification(context, notifTitle, notifMessage)
                } else {
                    launcher.launch(Manifest.permission.POST_NOTIFICATIONS)
                }
            } else {
                showNotification(context, notifTitle, notifMessage)
            }
            viewModel.onNotificationShown()
        }
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        "Daftar Titip.in",
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
                )
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(MaterialTheme.colorScheme.surface) // Background layar
                .padding(horizontal = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Spacer(modifier = Modifier.height(16.dp))

            // BUTTON YANG LEBIH CANTIK
            Button(
                onClick = { viewModel.loadData() },
                enabled = !isLoading,
                shape = RoundedCornerShape(50), // Bentuk Pil
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = Color.White
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                elevation = ButtonDefaults.buttonElevation(defaultElevation = 4.dp)
            ) {
                if (isLoading) {
                    CircularProgressIndicator(
                        color = Color.White,
                        modifier = Modifier.size(24.dp),
                        strokeWidth = 2.dp
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Sedang Memuat...")
                } else {
                    Icon(imageVector = Icons.Default.Refresh, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(text = "Ambil Data Titipan", fontWeight = FontWeight.SemiBold)
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            if (!isLoading && titipanList.isEmpty()) {
                // Tampilan saat data kosong
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("Belum ada titipan. Klik tombol di atas!", color = Color.Gray)
                }
            } else {
                // LIST CARD
                LazyColumn(
                    contentPadding = PaddingValues(bottom = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp) // Jarak antar kartu
                ) {
                    items(titipanList) { item ->
                        TitipanItemCard(item)
                    }
                }
            }
        }
    }
}

// --- KOMPONEN CARD TERPISAH (Agar lebih rapi) ---
@Composable
fun TitipanItemCard(item: Titipan) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp), // Sudut lebih membulat
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant // Warna kartu sedikit beda dari background
        )
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // 1. Icon Kotak di Kiri
            Box(
                modifier = Modifier
                    .size(50.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.ShoppingCart,
                    contentDescription = "Item",
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(24.dp)
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            // 2. Informasi Tengah (Nama Barang & Catatan)
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = item.nama_barang,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )

                // Info Pemesan dengan Icon kecil
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = null,
                        modifier = Modifier.size(12.dp),
                        tint = Color.Gray
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = item.pemesan,
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.Gray
                    )
                }

                Spacer(modifier = Modifier.height(4.dp))

                // Catatan dengan background tipis
                if (item.catatan.isNotEmpty()) {
                    Text(
                        text = "\"${item.catatan}\"",
                        style = MaterialTheme.typography.bodySmall,
                        fontStyle = androidx.compose.ui.text.font.FontStyle.Italic,
                        color = MaterialTheme.colorScheme.secondary
                    )
                }
            }

            // 3. Harga di Kanan
            Text(
                text = formatRupiah(item.estimasi_harga),
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.ExtraBold,
                color = MaterialTheme.colorScheme.primary
            )
        }
    }
}

// Fungsi helper untuk format Rupiah
fun formatRupiah(number: Int): String {
    val localeID = Locale("in", "ID")
    val formatRupiah = NumberFormat.getCurrencyInstance(localeID)
    // Hapus ,00 di belakang biar rapi
    return formatRupiah.format(number.toDouble()).replace(",00", "")
}