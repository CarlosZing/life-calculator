package com.zing.calculadoradevida

import android.app.DatePickerDialog
import android.os.Bundle
import android.widget.EditText
import android.widget.Spinner
import android.widget.TextView
import android.widget.Button
import android.widget.ArrayAdapter
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import java.util.*

class MainActivity : AppCompatActivity() {

    private lateinit var editTextDate: EditText
    private lateinit var spinnerTimeUnit: Spinner
    private lateinit var buttonCalculate: Button
    private lateinit var textViewResult: TextView  // Este es el TextView para mostrar el resultado

    private var birthDate: Calendar? = null // Para almacenar la fecha de nacimiento

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Habilitar borde a borde
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        // Configurar la ventana con márgenes del sistema (barra de estado y navegación)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Inicializar vistas
        editTextDate = findViewById(R.id.editTextDate)
        spinnerTimeUnit = findViewById(R.id.spinner)
        buttonCalculate = findViewById(R.id.button)
        textViewResult = findViewById(R.id.textView4)  // Usamos textViewResult

        // Configurar el Spinner con los valores de años, meses, días desde el array de strings
        val timeUnits = resources.getStringArray(R.array.opciones)
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, timeUnits)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerTimeUnit.adapter = adapter

        // Configurar el EditText para abrir el DatePicker
        editTextDate.setOnClickListener {
            showDatePickerDialog()
        }

        // Configurar el botón para calcular
        buttonCalculate.setOnClickListener {
            calculateAge()
        }
    }

    private fun showDatePickerDialog() {
        // Obtener la fecha actual
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH)

        // Crear el DatePickerDialog
        val datePickerDialog = DatePickerDialog(
            this,
            { _, selectedYear, selectedMonth, selectedDayOfMonth ->
                // Establecer la fecha seleccionada en el EditText
                val selectedDate = "$selectedDayOfMonth/${selectedMonth + 1}/$selectedYear"
                editTextDate.setText(selectedDate)

                // Guardar la fecha de nacimiento en el objeto Calendar
                birthDate = Calendar.getInstance().apply {
                    set(selectedYear, selectedMonth, selectedDayOfMonth)
                }
            },
            year, month, dayOfMonth
        )

        // Mostrar el DatePickerDialog
        datePickerDialog.show()
    }

    private fun calculateAge() {
        if (birthDate == null) {
            textViewResult.text =
                getString(R.string.error_select_date) // Mensaje de error si no se seleccionó una fecha
            return
        }

        val currentDate = Calendar.getInstance()
        val selectedUnit = spinnerTimeUnit.selectedItem.toString()

        // Calcular la diferencia en milisegundos
        val diffInMillis = currentDate.timeInMillis - birthDate!!.timeInMillis
        val diffInDays = diffInMillis / (1000 * 60 * 60 * 24)


        when (selectedUnit) {
            "Años" -> {
                val years = diffInDays / 365
                textViewResult.text = "Has vivido ${getString(R.string.Text4)} $years años."
            }

            "Meses" -> {
                val months = diffInDays / 30
                textViewResult.text = "Has vivido ${getString(R.string.Text4)} $months meses."
            }

            "Días" -> {
                textViewResult.text = "Has vivido ${getString(R.string.Text4)} $diffInDays días."

            }

            "Years" -> {
                val years = diffInDays / 365
                textViewResult.text = "You have lived ${getString(R.string.Text4)} $years years."
            }

            "Months" -> {
                val months = diffInDays / 30
                textViewResult.text = "you have lived ${getString(R.string.Text4)} $months months."
            }

            "Days" -> {
                textViewResult.text = "You have lived ${getString(R.string.Text4)} $diffInDays days."

            }
        }

    }
}
