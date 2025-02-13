package com.example.examen2evamovilespablomalcom

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ListView
import android.widget.Toast
import androidx.fragment.app.Fragment

class FragmentListadoBares : Fragment() {
    // FRAGMENT PARA LA VISTA DE LA LISTA DE LOS RESTAURANTES
    private lateinit var listViewRestaurantes: ListView
    private var adaptador: RestauranteAdapter? = null

    //declaramos el comuunicador
    private var comunicador: Comunicador? = null

    // Asegurar que la actividad implemente Comunicador
    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is Comunicador) {
            comunicador = context
        } else {
            throw RuntimeException("$context debe implementar Comunicador")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.listabares, container, false)
        listViewRestaurantes = view.findViewById(R.id.ListaBaresLista)

        adaptador?.let {
            listViewRestaurantes.adapter = it
        }


        if (adaptador == null) {
            adaptador = RestauranteAdapter(requireContext(), emptyList())
        }
        listViewRestaurantes.adapter = adaptador

        listViewRestaurantes.setOnItemClickListener { _, _, position, _ ->
            val sharedPreferences = requireContext().getSharedPreferences("UltimoBar", Context.MODE_PRIVATE)
            val editor = sharedPreferences.edit()


            val barSeleccionado = adaptador?.getItem(position) as? Bar
            barSeleccionado?.let {
                Toast.makeText(requireContext(), "Datos enviados", Toast.LENGTH_SHORT).show()
                comunicador?.enviarDatos(it.NombreBar, it.Direccion, it.Valoracion.toString(), it.Web, it.Latitud, it.Longitud)
                editor.putString("nombre", it.NombreBar)
                editor.putString("direccion", it.Direccion)
                editor.putString("valoracion", it.Valoracion.toString())
                editor.putString("web",  it.Web)
                editor.putFloat("latitud", it.Latitud.toFloat())
                editor.putFloat("longitud", it.Longitud.toFloat())
                editor.apply()
                Toast.makeText(requireContext(), "datos guardados", Toast.LENGTH_SHORT).show()
            }
        }



        return view
    }

    fun setAdapter(adapter: RestauranteAdapter) {
        this.adaptador = adapter
        view?.findViewById<ListView>(R.id.ListaBaresLista)?.adapter = adapter
    }

    fun actualizarLista(nuevaLista: List<Bar>) {
        adaptador = RestauranteAdapter(requireContext(), nuevaLista)
        listViewRestaurantes.adapter = adaptador
    }


}