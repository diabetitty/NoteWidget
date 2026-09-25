package it.local.notewidget

import android.app.Activity
import android.app.AlertDialog
import android.graphics.Color
import android.os.Bundle
import android.view.ViewGroup
import android.widget.*
import android.graphics.Typeface

class SettingsActivity : Activity() {
    private val fonts = arrayOf("Sans", "Serif", "Monospace")
    private val formats = arrayOf(
        "Venerdì 25 Settembre",
        "Ven 25 Set",
        "Venerdì 25 Set",
        "Ven 25 Settembre",
        "25 Settembre 2026",
        "25 Set 2026",
        "25/09/2026",
        "25/09"
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val root = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(28, 24, 28, 24)
        }
        addSection(root, "Formato della data") {
            choose("Formato", formats, SettingsRepository.dateFormat) {
                SettingsRepository.dateFormat = it
                NoteWidgetProvider.refresh(this)
            }
        }
        addSection(root, "Carattere delle note") {
            choose("Carattere", fonts, SettingsRepository.font) {
                SettingsRepository.font = it
                NoteWidgetProvider.refresh(this)
            }
        }
        addSize(root, "Dimensione testo", SettingsRepository.textSize) {
            SettingsRepository.textSize = it
            NoteWidgetProvider.refresh(this)
        }
        addColor(root, "Colore testo", SettingsRepository.textColor) {
            SettingsRepository.textColor = it
            NoteWidgetProvider.refresh(this)
        }
        addColor(root, "Sfondo widget", SettingsRepository.backgroundColor) {
            SettingsRepository.backgroundColor = it
            NoteWidgetProvider.refresh(this)
        }

        val title = TextView(this).apply {
            text = "Note di oggi"
            textSize = 22f
            setTypeface(null, Typeface.BOLD)
            setPadding(0, 30, 0, 10)
        }
        root.addView(title)

        addSection(root, "Carattere nota di oggi") {
            choose("Carattere", fonts, SettingsRepository.todayFont) {
                SettingsRepository.todayFont = it
                NoteWidgetProvider.refresh(this)
            }
        }
        addSize(root, "Dimensione nota di oggi", SettingsRepository.todayTextSize) {
            SettingsRepository.todayTextSize = it
            NoteWidgetProvider.refresh(this)
        }
        addColor(root, "Colore testo di oggi", SettingsRepository.todayTextColor) {
            SettingsRepository.todayTextColor = it
            NoteWidgetProvider.refresh(this)
        }
        addColor(root, "Sfondo nota di oggi", SettingsRepository.todayBackgroundColor) {
            SettingsRepository.todayBackgroundColor = it
            NoteWidgetProvider.refresh(this)
        }

        val scroll = ScrollView(this)
        scroll.addView(root)
        setContentView(scroll)
    }

    private fun addSection(root: LinearLayout, label: String, action: () -> Unit) {
        val b = Button(this).apply { text = label; setOnClickListener { action() } }
        root.addView(b)
    }

    private fun addSize(root: LinearLayout, label: String, value: Float, onSave: (Float) -> Unit) {
        val b = Button(this).apply {
            text = "$label: ${value.toInt()} sp"
            setOnClickListener {
                val input = EditText(this@SettingsActivity).apply {
                    inputType = 2
                    setText(value.toInt().toString())
                }
                AlertDialog.Builder(this@SettingsActivity)
                    .setTitle(label)
                    .setView(input)
                    .setNegativeButton("Annulla", null)
                    .setPositiveButton("OK") { _, _ ->
                        input.text.toString().toFloatOrNull()?.let {
                            onSave(it.coerceIn(8f, 48f))
                            text = "$label: ${it.toInt()} sp"
                        }
                    }.show()
            }
        }
        root.addView(b)
    }

    private fun addColor(root: LinearLayout, label: String, value: Int, onSave: (Int) -> Unit) {
        val b = Button(this).apply {
            text = label
            setTextColor(value)
            setOnClickListener {
                val colors = arrayOf("Nero", "Bianco", "Grigio", "Rosso", "Verde", "Blu", "Giallo")
                val values = intArrayOf(Color.BLACK, Color.WHITE, Color.DKGRAY, Color.RED, Color.GREEN, Color.BLUE, Color.YELLOW)
                AlertDialog.Builder(this@SettingsActivity)
                    .setTitle(label)
                    .setItems(colors) { _, which ->
                        onSave(values[which])
                        setTextColor(values[which])
                    }.show()
            }
        }
        root.addView(b)
    }

    private fun choose(title: String, items: Array<String>, selected: Int, save: (Int) -> Unit) {
        AlertDialog.Builder(this)
            .setTitle(title)
            .setSingleChoiceItems(items, selected) { dialog, which ->
                save(which)
                dialog.dismiss()
            }.show()
    }
}
