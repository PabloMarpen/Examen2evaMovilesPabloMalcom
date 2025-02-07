package com.example.examen2evamovilespablomalcom

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private var bares: List<Bar> = listOf()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        val botonCrear = findViewById<Button>(R.id.buttonIrCrear)
        val botonAcerca = findViewById<Button>(R.id.buttonAcercaDe)


        CoroutineScope(Dispatchers.Main).launch {
            try {

                val fragment = FragmentListadoBares()
                fragment.setAdapter(RestauranteAdapter(this@MainActivity, bares))
                showFragment(fragment)
            } catch (e: Exception) {
                Log.e("ListadoRestaurantes", "Error al obtener datos: ${e.message}", e)
            }
        }


        botonCrear.setOnClickListener {
            val intent = Intent(this, CrearBar::class.java)
            startActivity(intent)
        }
        botonAcerca.setOnClickListener{
            Toast.makeText(this, "Pablo Martin Peña", Toast.LENGTH_SHORT).show()
        }

    }

    private fun showFragment(fragment: Fragment) {
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.frameLayoutLista, fragment)
            .commit()
    }
}