package com.example.assignmentindividu.utils

import android.content.Context
import android.content.SharedPreferences

object FirstChecker {
    private const val PREFS_NAME = "MyAppPreferences"
    private const val FIRST_RUN_KEY = "isFirstRun"

    fun isFirstRun(context: Context): Boolean {
        val sharedPreferences: SharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        return sharedPreferences.getBoolean(FIRST_RUN_KEY, true)
    }

    fun setFirstRun(context: Context, isFirstRun: Boolean) {
        val sharedPreferences: SharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        with(sharedPreferences.edit()) {
            putBoolean(FIRST_RUN_KEY, isFirstRun)
            apply()
        }
    }

    // Add other utility methods here
}