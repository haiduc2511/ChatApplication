package com.example.chatapplication2.utils

import android.content.Context
import android.content.SharedPreferences

class SharedPreferenceManager(context: Context) {

    companion object {
        private const val PREF_NAME = "app_preferences"
        private const val MODE = Context.MODE_PRIVATE
    }

    private val preferences: SharedPreferences = context.getSharedPreferences(PREF_NAME, MODE)

    // Save a string value
    fun setString(key: String, value: String) {
        preferences.edit().putString(key, value).apply()
    }

    fun getString(key: String, defaultValue: String = ""): String {
        return preferences.getString(key, defaultValue) ?: defaultValue
    }

    // Save an integer value
    fun setInt(key: String, value: Int) {
        preferences.edit().putInt(key, value).apply()
    }

    fun getInt(key: String, defaultValue: Int = 0): Int {
        return preferences.getInt(key, defaultValue)
    }

    // Save a boolean value
    fun setBoolean(key: String, value: Boolean) {
        preferences.edit().putBoolean(key, value).apply()
    }

    fun getBoolean(key: String, defaultValue: Boolean = false): Boolean {
        return preferences.getBoolean(key, defaultValue)
    }

    // Remove a specific key
    fun remove(key: String) {
        preferences.edit().remove(key).apply()
    }

    // Clear all data
    fun clear() {
        preferences.edit().clear().apply()
    }
}