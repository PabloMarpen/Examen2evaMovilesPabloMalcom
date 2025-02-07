package com.example.examen2evamovilespablomalcom

import android.content.Context
import android.content.Intent
import android.os.Bundle
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


        listViewRestaurantes.setOnItemClickListener { _, _, position, _ ->
            val restauranteSeleccionado = adaptador?.getItem(position)

            //POR AQUI SE PASA AL OTRO
        }



        return view
    }

    fun setAdapter(adapter: RestauranteAdapter) {
        this.adaptador = adapter
        view?.findViewById<ListView>(R.id.ListaBaresList)?.adapter = adapter
    }
}