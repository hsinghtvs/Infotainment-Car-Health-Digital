package com.example.infotainment_car_health_digital.repository

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

val Context.DATA_STORE by preferencesDataStore(name = "dataStore")

class AppSettings @Inject constructor(
    private val context: Context,
) {
    private val accessToken = stringPreferencesKey("access_token")
    private val refreshToken = stringPreferencesKey("refresh_token")
    private val estimateToken = stringPreferencesKey("estimate_token")

    fun getAccessToken(): String? {
        return runBlocking { context.DATA_STORE.data.first() }[accessToken] ?: "Empty"
    }

    fun getRefreshToken(): String? {
        return runBlocking { context.DATA_STORE.data.first() }[refreshToken] ?: "Empty"
    }

    fun writeToken(
        accessToken: String,
        refreshToken: String
    ) {
        runBlocking {
            context.DATA_STORE.edit { preferences ->
                preferences[this@AppSettings.accessToken] = accessToken
                preferences[this@AppSettings.refreshToken] = refreshToken
            }
        }
    }

    fun getEstimateToken(): String? {
        return runBlocking { context.DATA_STORE.data.first() }[estimateToken] ?: "Empty"
    }


    fun writeEstimateToken(estimateToken: String) {
        runBlocking {
            context.DATA_STORE.edit { preferences ->
                preferences[this@AppSettings.estimateToken] = estimateToken
            }
        }
    }
}