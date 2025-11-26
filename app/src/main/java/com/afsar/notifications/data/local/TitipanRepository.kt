package com.afsar.notifications.data.local

import android.content.Context
import com.afsar.notifications.data.model.Titipan
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class TitipanRepository(private val context: Context) {

    suspend fun getTitipanData(): List<Titipan> {
        return withContext(Dispatchers.IO) {
            try {
                Thread.sleep(2000)

                val jsonString = context.assets.open("titipin-data.json")
                    .bufferedReader()
                    .use { it.readText() }

                val listType = object : TypeToken<List<Titipan>>() {}.type
                Gson().fromJson(jsonString, listType)
            } catch (e: Exception) {
                emptyList()
            }
        }
    }
}