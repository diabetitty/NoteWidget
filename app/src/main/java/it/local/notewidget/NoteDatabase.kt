package it.local.notewidget

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.content.ContentValues

class NoteDatabase(context: Context) :
    SQLiteOpenHelper(context, "notes.db", null, 1) {

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL("""
            CREATE TABLE notes (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                text TEXT NOT NULL,
                date TEXT NOT NULL
            )
        """.trimIndent())
        db.execSQL("CREATE INDEX idx_notes_date ON notes(date)")
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {}

    fun getAll(): List<Note> {
        val result = mutableListOf<Note>()
        readableDatabase.query(
            "notes", arrayOf("id", "text", "date"),
            null, null, null, null, "date DESC, id DESC"
        ).use { c ->
            while (c.moveToNext()) {
                result.add(Note(c.getLong(0), c.getString(1), c.getString(2)))
            }
        }
        return result
    }

    fun get(id: Long): Note? {
        readableDatabase.query(
            "notes", arrayOf("id", "text", "date"), "id=?",
            arrayOf(id.toString()), null, null, null
        ).use { c ->
            if (c.moveToFirst()) return Note(c.getLong(0), c.getString(1), c.getString(2))
        }
        return null
    }

    fun insert(text: String, date: String): Long {
        val v = ContentValues().apply {
            put("text", text)
            put("date", date)
        }
        return writableDatabase.insert("notes", null, v)
    }

    fun update(id: Long, text: String, date: String) {
        val v = ContentValues().apply {
            put("text", text)
            put("date", date)
        }
        writableDatabase.update("notes", v, "id=?", arrayOf(id.toString()))
    }

    fun delete(id: Long) {
        writableDatabase.delete("notes", "id=?", arrayOf(id.toString()))
    }
}
