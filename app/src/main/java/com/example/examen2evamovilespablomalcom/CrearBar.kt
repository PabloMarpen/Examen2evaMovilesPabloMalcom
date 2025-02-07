package com.example.examen2evamovilespablomalcom

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.util.concurrent.TimeUnit

class CrearBar : AppCompatActivity() {

    private lateinit var id: EditText
    private lateinit var nombre: EditText
    private lateinit var direccion: EditText
    private lateinit var valoracion: EditText
    private lateinit var latitud: EditText
    private lateinit var longitud: EditText
    private lateinit var web: EditText

    private lateinit var buttonInsertar: Button
    private lateinit var buttonBorrar: Button
    private lateinit var buttonEditar: Button
    private lateinit var dbHandler: DatabaseHelper
    //private lateinit var recyclerView: RecyclerView
    private val REQUEST_CODE_PERMISSIONS = 101

    override fun onCreate(savedInstanceState: Bundle?) {


        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.crear_bar)

        id = findViewById(R.id.editText)
        nombre = findViewById(R.id.editTextNombre)
        direccion = findViewById(R.id.editTextDireccion)
        valoracion = findViewById(R.id.editTextValoracion)
        latitud = findViewById(R.id.editTextLatitud)
        longitud = findViewById(R.id.editTextLongitud)
        web = findViewById(R.id.editTextWeb)

        buttonInsertar = findViewById(R.id.ButtonCrear)
        buttonBorrar = findViewById(R.id.buttonBorrar)
        buttonEditar = findViewById(R.id.buttonActualizar)

        dbHandler = DatabaseHelper(this)
        //recyclerView.layoutManager = LinearLayoutManager(this)

        buttonInsertar.setOnClickListener { addBar()
            }
        buttonBorrar.setOnClickListener { borrarBar() }
        buttonEditar.setOnClickListener { actualizarBar() }


    }

    // Métodoo para limpiar los campos de texto.
    private fun clearEditTexts() {
        id.text.clear()
        nombre.text.clear()
        direccion.text.clear()
        valoracion.text.clear()
        latitud.text.clear()
        longitud.text.clear()
        web.text.clear()
    }



    private fun actualizarBar() {
        val id = id.text.toString()
        val nombre = nombre.text.toString()
        val direccion = direccion.text.toString()
        val valoracion = valoracion.text.toString()
        val latitud = latitud.text.toString()
        val longitud = longitud.text.toString()
        val web = web.text.toString()

        if (nombre.isNotEmpty() && direccion.isNotEmpty() && id.isNotEmpty() && valoracion.isNotEmpty() && latitud.isNotEmpty()  && longitud.isNotEmpty()  && web.isNotEmpty()) {
            val bar = Bar(NombreBar = nombre, Direccion = direccion, id = id.toInt(), Valoracion = valoracion.toInt(), Latitud = latitud.toDouble(), Longitud = longitud.toDouble(), Web = web)
            val status = dbHandler.updateBar(bar)
            if (status > -1) {
                Toast.makeText(applicationContext, "Bar actualizado", Toast.LENGTH_LONG).show()
                clearEditTexts()
                viewBares()
            }
        } else {
            Toast.makeText(applicationContext, "todo es obligatorio", Toast.LENGTH_LONG)
                .show()
        }
    }

    private fun borrarBar() {
        val id = id.text.toString()
        if (id.isNotEmpty()) {
            val nota = Bar(id = id.toInt())
            val status = dbHandler.deleteBar(nota)
            if (status > -1) {
                Toast.makeText(applicationContext, "Nota eliminada", Toast.LENGTH_LONG).show()
                clearEditTexts()
                viewBares()
            }else {
                Toast.makeText(applicationContext, "id es requerido", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun addBar() {
        val id = id.text.toString()
        val nombre = nombre.text.toString()
        val direccion = direccion.text.toString()
        val valoracion = valoracion.text.toString()
        val latitud = latitud.text.toString()
        val longitud = longitud.text.toString()
        val web = web.text.toString()

        if (nombre.isNotEmpty() && direccion.isNotEmpty() && id.isNotEmpty() && valoracion.isNotEmpty() && latitud.isNotEmpty()  && longitud.isNotEmpty()  && web.isNotEmpty()) {
            val bar = Bar(NombreBar = nombre, Direccion = direccion, id = id.toInt(), Valoracion = valoracion.toInt(), Latitud = latitud.toDouble(), Longitud = longitud.toDouble(), Web = web)
            val status = dbHandler.addBar(bar)
            if (status > -1) {
                if (checkPermissions()) {
                    createNotificationChannel()
                    showNotification("Bar creado", "Se ha creado el bar")
                } else {

                    requestPermissions()
                }
                Toast.makeText(applicationContext, "Bar agregado", Toast.LENGTH_LONG).show()
                clearEditTexts()
                viewBares()

            }
        } else {
            Toast.makeText(applicationContext, "todo es obligatorio", Toast.LENGTH_LONG).show()
        }
    }

    private fun viewBares() {
        val NotaLista = dbHandler.getAllBares()
        //val adapter = BarAdapter(NotaLista)
        //recyclerView.layoutManager = LinearLayoutManager(this)
        //recyclerView.adapter = adapter
    }


    // Función para mostrar una notificación inmediata
    private fun showNotification(title: String, message: String) {
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val builder = NotificationCompat.Builder(this, "canal_notificaciones")
            .setSmallIcon(R.drawable.ic_launcher_foreground) // Icono de la notificación
            .setContentTitle(title)
            .setContentText(message)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true) // La notificación desaparece al tocarla

        notificationManager.notify(1, builder.build()) // ID de la notificación
    }


    // Crear el canal de notificación (obligatorio en Android 8.0+)
    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                "canal_notificaciones",
                "Canal de Notificaciones",
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "Canal para mostrar notificaciones"
            }

            val notificationManager = getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannel(channel)
        }
    }

    // Verifica si los permisos necesarios están concedidos
    private fun checkPermissions(): Boolean {
        val notificationPermission = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            ContextCompat.checkSelfPermission(this, android.Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED
        } else {
            true // No es necesario pedir permiso en versiones anteriores
        }
        return notificationPermission
    }

    // Solicita permisos en tiempo de ejecución
    private fun requestPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            ActivityCompat.requestPermissions(this,
                arrayOf(android.Manifest.permission.POST_NOTIFICATIONS),
                REQUEST_CODE_PERMISSIONS
            )
        }
    }

    // Maneja la respuesta del usuario cuando acepta o niega permisos
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permiso concedido, mostrar notificación
                showNotification("¡Permiso concedido!", "Ahora recibirás notificaciones.")
            } else {
                // Permiso denegado, mostrar mensaje
                showNotification("Permiso denegado", "No puedes recibir notificaciones.")
            }
        }
    }
}