package com.volla.simpleappstheme

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.util.Log
import com.simplemobiletools.commons.extensions.baseConfig
import com.simplemobiletools.commons.helpers.MyContentProvider
import com.simplemobiletools.commons.models.SharedTheme

class ColorChangeReceiver : BroadcastReceiver() {

    private val LOG_TAG = "ColorChangeResceiver"

    override fun onReceive(context: Context, intent: Intent) {

        Intent().apply {
            action = MyContentProvider.SHARED_THEME_ACTIVATED
            context.sendBroadcast(this)
        }

        Log.d(LOG_TAG, "Shared colors activated")

        val textColor = intent.getIntExtra("com.volla.simpleappstheme.param.TEXT_COLOR", Color.WHITE)
        val backgroundColor = intent.getIntExtra("com.volla.simpleappstheme.param.BACKGROUND_COLOR", Color.BLACK)
        val primaryColor = intent.getIntExtra("com.volla.simpleappstheme.param.PRIMARY_COLOR", Color.BLACK)
        val accentColor = intent.getIntExtra("com.volla.simpleappstheme.param.ACCENT_COLOR", Color.WHITE)
        val navigationBarColor = intent.getIntExtra("com.volla.simpleappstheme.param.NAVIGATION_BAR_COLOR", Color.BLACK)
        val newSharedTheme = SharedTheme(textColor, backgroundColor, primaryColor, context.baseConfig.appIconColor, navigationBarColor, 0, accentColor)

        try {
            val contentValues = MyContentProvider.fillThemeContentValues(newSharedTheme)
            context.contentResolver.update(MyContentProvider.MY_CONTENT_URI, contentValues, null, null)
        } catch (e: Exception) {
            Log.e(LOG_TAG, "Failed to update colors $e")
        }

        Intent().apply {
            action = MyContentProvider.SHARED_THEME_UPDATED
            context.sendBroadcast(this)
        }

        Log.d(LOG_TAG, "Shared colors updated")
    }
}