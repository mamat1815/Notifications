package com.afsar.notifications

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import com.afsar.notifications.ui.viewmodel.TitipanViewModel
import com.afsar.notifications.utility.showNotification
@Composable
fun TitipanScreen(viewModel: TitipanViewModel = viewModel()) {
    val titipanList by viewModel.titipanList.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val processFinished by viewModel.processFinished.collectAsState()

    val notifMessage by viewModel.notificationMessage.collectAsState()

    val context = LocalContext.current

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { isGranted ->
            if (isGranted) showNotification(context, notifMessage)
        }
    )

    LaunchedEffect(processFinished) {
        if (processFinished) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                if (ContextCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED) {

                    showNotification(context, notifMessage)
                } else {
                    launcher.launch(Manifest.permission.POST_NOTIFICATIONS)
                }
            } else {

                showNotification(context, notifMessage)
            }
            viewModel.onNotificationShown()
        }
    }

    Scaffold { innerPadding->
        Column(
            modifier = Modifier.fillMaxSize().padding(innerPadding).padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = "Daftar Titip.in", style = MaterialTheme.typography.headlineMedium)

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = { viewModel.loadData() },
                enabled = !isLoading
            ) {
                Text(text = if (isLoading) "Memuat Data..." else "Ambil Data Titipan")
            }

            Spacer(modifier = Modifier.height(16.dp))

            if (isLoading) {
                CircularProgressIndicator()
            } else {
                LazyColumn {
                    items(titipanList) { item ->
                        Card(
                            modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Text(text = item.nama_barang, style = MaterialTheme.typography.titleMedium)
                                Text(text = "Pemesan: ${item.pemesan}")
                                Text(text = "Harga: Rp ${item.estimasi_harga}")
                                Text(text = "Note: ${item.catatan}", style = MaterialTheme.typography.bodySmall)
                            }
                        }
                    }
                }
            }
        }
    }
}