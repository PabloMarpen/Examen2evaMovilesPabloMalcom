package com.example.examen2evamovilespablomalcom

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.w3c.dom.Text

class MainActivity : AppCompatActivity(), Comunicador {
    private var bares: List<Bar> = listOf()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val botonCrear = findViewById<Button>(R.id.buttonIrCrear)
        val botonAcerca = findViewById<Button>(R.id.buttonAcercaDe)
        val textoArriba = findViewById<TextView>(R.id.textView)

        val dbHandler = DatabaseHelper(this)
        val bares = dbHandler.getAllBares()  // 👈 Recupera los bares de la BD
        val fragment = FragmentListadoBares()

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.frameLayoutLista, FragmentListadoBares())
                .commit()

            supportFragmentManager.beginTransaction()
                .replace(R.id.frameLayoutDetalle, FragmentoDetalleBares())
                .commit()
        }


        fragment.setAdapter(RestauranteAdapter(this, bares)) // 👈 Pasa la lista obtenida
        showFragment(fragment)

        botonCrear.setOnClickListener {
            val intent = Intent(this, CrearBar::class.java)
            startActivity(intent)
        }

        botonAcerca.setOnClickListener {
            textoArriba.text = "Pablo"
        }
    }


    private fun showFragment(fragment: Fragment) {
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.frameLayoutLista, fragment)
            .commit()
    }



    override fun enviarDatos(
        nombre: String,
        direccion: String,
        valoracion: String,
        web: String,
        latitud: Double,
        longitud: Double
    ) {
        val fragmentoDetalle = supportFragmentManager.findFragmentById(R.id.frameLayoutDetalle) as? FragmentoDetalleBares
        fragmentoDetalle?.actualizarTexto(nombre, direccion, valoracion, web, latitud, longitud)
    }





    override fun onResume() {
        super.onResume()
        val dbHandler = DatabaseHelper(this)
        bares = dbHandler.getAllBares() // 🔄 Recargar los bares
        val fragment = supportFragmentManager.findFragmentById(R.id.frameLayoutLista) as? FragmentListadoBares
        fragment?.actualizarLista(bares) // ✅ Actualiza la lista en el fragmento

        val sharedPreferences = this.getSharedPreferences("UltimoBar", Context.MODE_PRIVATE)

        val nombre: String = sharedPreferences.getString("nombre", "Desconocido").toString()
        val direccion : String = sharedPreferences.getString("direccion", "Desconocida").toString()
        val valoracion : String = sharedPreferences.getString("valoracion", "Desconocida").toString()
        val web : String = sharedPreferences.getString("web", "Desconocida").toString()
        val latitud : Double = sharedPreferences.getFloat("latitud", 0f).toDouble()
        val longitud : Double = sharedPreferences.getFloat("longitud", 0f).toDouble()

        enviarDatos(nombre, direccion, valoracion, web, latitud, longitud)
    }


}