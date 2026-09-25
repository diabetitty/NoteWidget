package it.local.notewidget

import android.content.Context
import android.graphics.Color

object SettingsRepository {
    private const val PREF = "settings"

    fun prefs(c: Context) = c.getSharedPreferences(PREF, Context.MODE_PRIVATE)

    var dateFormat: Int
        get() = prefs(App.context).getInt("dateFormat", 0)
        set(v) { prefs(App.context).edit().putInt("dateFormat", v).apply() }

    var font: Int
        get() = prefs(App.context).getInt("font", 0)
        set(v) { prefs(App.context).edit().putInt("font", v).apply() }

    var textSize: Float
        get() = prefs(App.context).getFloat("textSize", 16f)
        set(v) { prefs(App.context).edit().putFloat("textSize", v).apply() }

    var textColor: Int
        get() = prefs(App.context).getInt("textColor", Color.DKGRAY)
        set(v) { prefs(App.context).edit().putInt("textColor", v).apply() }

    var backgroundColor: Int
        get() = prefs(App.context).getInt("backgroundColor", Color.WHITE)
        set(v) { prefs(App.context).edit().putInt("backgroundColor", v).apply() }

    var todayFont: Int
        get() = prefs(App.context).getInt("todayFont", 0)
        set(v) { prefs(App.context).edit().putInt("todayFont", v).apply() }

    var todayTextSize: Float
        get() = prefs(App.context).getFloat("todayTextSize", 18f)
        set(v) { prefs(App.context).edit().putFloat("todayTextSize", v).apply() }

    var todayTextColor: Int
        get() = prefs(App.context).getInt("todayTextColor", Color.BLACK)
        set(v) { prefs(App.context).edit().putInt("todayTextColor", v).apply() }

    var todayBackgroundColor: Int
        get() = prefs(App.context).getInt("todayBackgroundColor", 0xFFFFE58A.toInt())
        set(v) { prefs(App.context).edit().putInt("todayBackgroundColor", v).apply() }
}
