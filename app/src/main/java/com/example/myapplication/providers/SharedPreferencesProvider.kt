package com.example.myapplication.providers

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import androidx.core.content.edit
import javax.inject.Inject

private const val SP_SETTINGS = "app_settings"

class SharedPreferencesProvider @Inject constructor(context: Context) {
    private var sharedPreferences: SharedPreferences = context.getSharedPreferences(SP_SETTINGS, MODE_PRIVATE)

    fun savePreferencesInt(
        key: String, value: Int?
    ) {
        sharedPreferences.edit { putInt(key, value ?: 0) }
    }

    fun getPreferencesInt(key: String): Int = sharedPreferences.getInt(key, 0)

    fun savePreferencesString(
        key: String, value: String?
    ) {
        sharedPreferences.edit { putString(key, value) }
    }

    fun getPreferencesString(
        key: String, defValue: String
    ): String? = sharedPreferences.getString(key, defValue)
}