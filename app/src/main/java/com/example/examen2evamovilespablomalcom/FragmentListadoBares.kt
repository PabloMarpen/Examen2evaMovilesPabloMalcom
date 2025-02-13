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

    // paso 1 se envian los datos
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // 1. Se infla el layout que contiene la lista de bares.
        val view = inflater.inflate(R.layout.listabares, container, false)
        listViewRestaurantes = view.findViewById(R.id.ListaBaresLista)

        // 2. Se configura el adaptador para la lista, asignándolo si no está ya definido.
        adaptador?.let {
            listViewRestaurantes.adapter = it
        }


        if (adaptador == null) {
            adaptador = RestauranteAdapter(requireContext(), emptyList())
        }
        listViewRestaurantes.adapter = adaptador

        // para cuando se haga clic en la lista
        listViewRestaurantes.setOnItemClickListener { _, _, position, _ ->
            val sharedPreferences = requireContext().getSharedPreferences("UltimoBar", Context.MODE_PRIVATE)
            val editor = sharedPreferences.edit()


            val barSeleccionado = adaptador?.getItem(position) as? Bar
            barSeleccionado?.let {
                Toast.makeText(requireContext(), "Datos enviados", Toast.LENGTH_SHORT).show()

                // 4. Se envían los datos al comunicador para que otro fragmento pueda mostrarlos.
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

    //    - `setAdapter()`: Permite asignar un adaptador externo a la lista.
    fun setAdapter(adapter: RestauranteAdapter) {
        this.adaptador = adapter
        view?.findViewById<ListView>(R.id.ListaBaresLista)?.adapter = adapter
    }

    //    - `actualizarLista()`: Recibe una nueva lista de bares y actualiza la vista.
    fun actualizarLista(nuevaLista: List<Bar>) {
        adaptador = RestauranteAdapter(requireContext(), nuevaLista)
        listViewRestaurantes.adapter = adaptador
    }


}