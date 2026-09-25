package it.local.notewidget

import android.content.Intent
import android.widget.RemoteViews
import android.widget.RemoteViewsService
import android.graphics.Typeface

class NoteWidgetService : RemoteViewsService() {
    override fun onGetViewFactory(intent: Intent): RemoteViewsFactory {
        return Factory(applicationContext)
    }

    private class Factory(private val context: android.content.Context) : RemoteViewsFactory {
        private var notes: List<Note> = emptyList()

        override fun onCreate() {}
        override fun onDestroy() {}
        override fun getCount() = notes.size
        override fun getViewTypeCount() = 1
        override fun getItemId(position: Int) = notes[position].id
        override fun hasStableIds() = true
        override fun getLoadingView(): RemoteViews? = null

        override fun onDataSetChanged() {
            notes = NoteDatabase(context).getAll()
        }

        override fun getViewAt(position: Int): RemoteViews {
            val n = notes[position]
            val today = n.date == DateUtils.today()
            val v = RemoteViews(context.packageName, R.layout.widget_item)

            val font = if (today) SettingsRepository.todayFont else SettingsRepository.font
            val size = if (today) SettingsRepository.todayTextSize else SettingsRepository.textSize
            val color = if (today) SettingsRepository.todayTextColor else SettingsRepository.textColor
            val bg = if (today) SettingsRepository.todayBackgroundColor else 0x00FFFFFF

            v.setTextViewText(R.id.note_date, DateUtils.format(n.date, SettingsRepository.dateFormat))
            v.setTextViewText(R.id.note_text, n.text)
            v.setTextViewTextSize(R.id.note_text, 2, size)
            v.setTextColor(R.id.note_date, color)
            v.setTextColor(R.id.note_text, color)
            v.setInt(R.id.note_item, "setBackgroundColor", bg)
            v.setTypeface(R.id.note_text, "sans-serif", Typeface.NORMAL)
            when (font) {
                1 -> v.setTypeface(R.id.note_text, "serif", Typeface.NORMAL)
                2 -> v.setTypeface(R.id.note_text, "monospace", Typeface.NORMAL)
            }

            val fillIn = Intent().apply { putExtra("noteId", n.id) }
            v.setOnClickFillInIntent(R.id.note_item, fillIn)
            return v
        }
    }
}
