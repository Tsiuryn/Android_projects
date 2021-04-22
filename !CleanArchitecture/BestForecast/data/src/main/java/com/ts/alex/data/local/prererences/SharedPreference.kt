package com.ts.alex.data.local.prererences

import android.content.Context
import android.util.Log

class SharedPreference(private val context: Context) {
    private val PREFERENCES = "com.ts.alex.data.local.prererences.my_preferences"

    private val GET_CURRENT_USER = "com.ts.alex.data.local.prererences.current_user"
    fun getUser(): String? {
        val user = "{\"email\":\"\",\"name\":\"\",\"password\":\"\"}"
        val preferences = context.getSharedPreferences(PREFERENCES, Context.MODE_PRIVATE)
        val userText = preferences.getString(GET_CURRENT_USER, user)
        return userText

    }

    fun saveUser(user: String) {
        val editor = context.getSharedPreferences(PREFERENCES, Context.MODE_PRIVATE)
        editor.edit().putString(GET_CURRENT_USER, user).apply()
    }

    private val GET_CITY = "com.ts.alex.data.local.prererences.saved_city"
    fun getCity(): String? {
        val preferences = context.getSharedPreferences(PREFERENCES, Context.MODE_PRIVATE)
        val userText = preferences.getString(GET_CITY, "")
        return userText

    }

    fun saveCity(city: String) {
        val editor = context.getSharedPreferences(PREFERENCES, Context.MODE_PRIVATE)
        editor.edit().putString(GET_CITY, city).apply()
    }

    private val GET_IS_REGISTERED = "com.ts.alex.data.local.prererences.is_registered"
    fun isRegistered(): Boolean {
        val preferences = context.getSharedPreferences(PREFERENCES, Context.MODE_PRIVATE)
        return preferences.getBoolean(GET_IS_REGISTERED, false)
    }

    fun setRegistered(isRegistered: Boolean) {
        val editor = context.getSharedPreferences(PREFERENCES, Context.MODE_PRIVATE)
        editor.edit().putBoolean(GET_IS_REGISTERED, isRegistered).apply()
    }
}