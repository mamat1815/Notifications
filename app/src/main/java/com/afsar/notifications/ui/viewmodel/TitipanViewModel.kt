package com.afsar.notifications.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.afsar.notifications.data.model.Titipan
import com.afsar.notifications.data.local.TitipanRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch


class TitipanViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = TitipanRepository(application)

    private val _titipanList = MutableStateFlow<List<Titipan>>(emptyList())
    val titipanList = _titipanList.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    private val _processFinished = MutableStateFlow(false)
    val processFinished = _processFinished.asStateFlow()

    private val _notificationMessage = MutableStateFlow("")
    val notificationMessage = _notificationMessage.asStateFlow()

    fun loadData() {
        viewModelScope.launch {
            _isLoading.value = true
            _processFinished.value = false

            val data = repository.getTitipanData()

            _titipanList.value = data
            _isLoading.value = false

            if (data.isNotEmpty()) {
                val randomItem = data.random()
                _notificationMessage.value = "Jangan lupa! ${randomItem.pemesan} nitip ${randomItem.nama_barang}."
            } else {
                _notificationMessage.value = "Data berhasil dimuat (Kosong)."
            }

            _processFinished.value = true
        }
    }

    fun onNotificationShown() {
        _processFinished.value = false
    }
}