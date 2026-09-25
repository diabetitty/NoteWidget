package it.local.notewidget

import android.app.Activity
import android.app.AlertDialog
import android.app.DatePickerDialog
import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.view.Gravity
import android.widget.*
import java.time.LocalDate

class MainActivity : Activity() {
    private lateinit var db: NoteDatabase
    private var noteId: Long? = null
    private lateinit var text: EditText
    private lateinit var dateButton: Button
    private var date = LocalDate.now()

    override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    try {
        db = NoteDatabase(this)
        noteId = intent.getLongExtra("noteId", -1L).takeIf { it > 0 }

        noteId?.let { db.get(it) }?.let {
            text = EditText(this).also { e -> e.setText(it.text) }
            date = LocalDate.parse(it.date)
        } ?: run {
            text = EditText(this)
        }

        buildUi()

    } catch (e: Throwable) {
        val errorText = TextView(this).apply {
            text = "ERRORE:\n\n${e.javaClass.name}\n\n${e.message}\n\n${e.stackTraceToString()}"
            textSize = 14f
            setPadding(30, 30, 30, 30)
        }

        setContentView(
            ScrollView(this).apply {
                addView(errorText)
            }
        )
    }
}

    private fun buildUi() {
        val root = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(28, 24, 28, 24)
        }
        val title = TextView(this).apply {
            text = if (noteId == null) "Nuova nota" else "Modifica nota"
            textSize = 24f
            setTypeface(null, Typeface.BOLD)
            setPadding(0, 0, 0, 20)
        }
        dateButton = Button(this).apply {
            updateDateText()
            setOnClickListener { chooseDate() }
        }
        text.apply {
            hint = "Scrivi la nota..."
            textSize = 18f
            minLines = 5
            gravity = Gravity.TOP
        }

        root.addView(title)
        root.addView(dateButton)
        root.addView(text, LinearLayout.LayoutParams(-1, 0, 1f))

        val row = LinearLayout(this).apply {
            orientation = LinearLayout.HORIZONTAL
            gravity = Gravity.END
        }
        val settings = Button(this).apply {
            this.text = "Impostazioni"
            setOnClickListener { startActivity(Intent(this@MainActivity, SettingsActivity::class.java)) }
        }
        val delete = Button(this).apply {
            this.text = "Elimina"
            visibility = if (noteId == null) Button.GONE else Button.VISIBLE
            setOnClickListener { confirmDelete() }
        }
        val save = Button(this).apply {
            this.text = "Salva"
            setOnClickListener { saveNote() }
        }
        row.addView(settings)
        row.addView(delete)
        row.addView(save)
        root.addView(row)
        setContentView(root)
    }

    private fun updateDateText() {
    dateButton.text = "Data: " + date.toString()
}
    private fun chooseDate() {
        DatePickerDialog(this, { _, y, m, d ->
            date = LocalDate.of(y, m + 1, d)
            updateDateText()
        }, date.year, date.monthValue - 1, date.dayOfMonth).show()
    }

    private fun saveNote() {
        val value = text.text.toString().trim()
        if (value.isEmpty()) {
            text.error = "Inserisci una nota"
            return
        }
        if (noteId == null) db.insert(value, date.toString())
        else db.update(noteId!!, value, date.toString())
        NoteWidgetProvider.refresh(this)
        finish()
    }

    private fun confirmDelete() {
        AlertDialog.Builder(this)
            .setTitle("Eliminare la nota?")
            .setMessage("La nota verrà eliminata dal telefono.")
            .setNegativeButton("Annulla", null)
            .setPositiveButton("Elimina") { _, _ ->
                noteId?.let { db.delete(it) }
                NoteWidgetProvider.refresh(this)
                finish()
            }.show()
    }
}
