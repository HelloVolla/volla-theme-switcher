package com.volla.simpleappstheme

import android.content.*
import android.graphics.Color
import android.util.Log
import com.simplemobiletools.commons.extensions.baseConfig
import com.simplemobiletools.commons.helpers.MyContentProvider
import com.simplemobiletools.commons.models.SharedTheme

class MainBootReceiver : BroadcastReceiver() {
    private val LOG_TAG = "VollaSimpleAppsTheme"

    override fun onReceive(context: Context, intent: Intent) {
        Log.d(LOG_TAG, "Setting colors")

        val pref = context.getSharedPreferences("MainPref", 0)

        val editor = pref.edit()
        val isApplied = pref.getBoolean("themes_applied", false)

        if (!isApplied) {
            Intent().apply {
                action = MyContentProvider.SHARED_THEME_ACTIVATED
                context.sendBroadcast(this)
            }

            Log.d(LOG_TAG, "Shared colors activated")
            val newSharedTheme = SharedTheme(Color.WHITE, Color.BLACK, Color.BLACK, context.baseConfig.appIconColor, Color.BLACK, 0, Color.WHITE)
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
            editor.putBoolean("themes_applied", true)
            editor.commit()
            Log.d(LOG_TAG, "Shared colors updated")
        } else {
            Log.d(LOG_TAG, "Colors already set")
        }
    }
}
