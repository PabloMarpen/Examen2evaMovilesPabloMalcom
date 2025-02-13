package com.example.examen2evamovilespablomalcom

import android.app.SearchManager
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions


class FragmentoDetalleBares : Fragment() , OnMapReadyCallback {

    private var NombreBar: String? = null
    private var Direccion: String? = null
    private var Valoracion: String? = null
    private var Web: String? = null
    private var Latitud: Double? = null
    private var Longitud: Double? = null


    private lateinit var mMap: GoogleMap
    private lateinit var mapView: MapView
    private var googleMap: GoogleMap? = null


    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        outState.putString("nombre", NombreBar)
        outState.putString("direccion", Direccion)
        outState.putString("valor", Valoracion)
        outState.putString("web", Web)
        Latitud?.let { outState.putDouble("latitud", it) }
        Longitud?.let { outState.putDouble("longitud", it) }
        mapView.onSaveInstanceState(outState)




    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.info_bares, container, false)

        // esto es para cuando le des clic a la web del restaurante te lleve a la web del restaurante
        val webRestaurante = view.findViewById<TextView>(R.id.textViewWeb)
        webRestaurante.setOnClickListener {
            val intent = Intent(Intent.ACTION_WEB_SEARCH)
            intent.putExtra(SearchManager.QUERY, webRestaurante.text)
            startActivity(intent)

        }

        if (savedInstanceState != null) {
            NombreBar = savedInstanceState.getString("nombre")
            Direccion = savedInstanceState.getString("direccion")
            Valoracion = savedInstanceState.getString("valor")
            Web = savedInstanceState.getString("web")

            view.findViewById<TextView>(R.id.textViewNombreBar).text = NombreBar
            view.findViewById<TextView>(R.id.textViewDireccion).text = Direccion
            view.findViewById<TextView>(R.id.textViewValoracion).text = Valoracion
            view.findViewById<TextView>(R.id.textViewWeb).text = Web


        }

        // Configurar MapView
        mapView = view.findViewById(R.id.mapView)
        mapView.onCreate(savedInstanceState)
        mapView.getMapAsync(this) // Llamar a OnMapReadyCallback

        // Devolvemos la vista inflada.
        return view
    }

    // paso 3 se recibe la informacion del fragmento
    fun actualizarTexto(nombre: String, direccion: String, valoracion: String, web: String, latitud: Double, longitud: Double) {

        NombreBar = nombre
        Direccion = direccion
        Valoracion = valoracion
        Web = web
        Latitud = latitud
        Longitud = longitud

        view?.findViewById<TextView>(R.id.textViewNombreBar)?.text = NombreBar
        view?.findViewById<TextView>(R.id.textViewDireccion)?.text = Direccion
        view?.findViewById<TextView>(R.id.textViewValoracion)?.text = Valoracion
        view?.findViewById<TextView>(R.id.textViewWeb)?.text = Web


        if (::mMap.isInitialized){
            googleMap = mMap
            googleMap?.let { actualizarMapa() }
        }
    }

    private fun actualizarMapa() {
        if (Latitud != null && Longitud != null) {
            val bar = LatLng(Latitud!!, Longitud!!)
            mMap.clear()
            mMap.addMarker(MarkerOptions().position(bar).title(Web ?: "Ubicación"))
            mMap.moveCamera(CameraUpdateFactory.newLatLng(bar))
            mMap.setOnMarkerClickListener { marker ->
                val intent = Intent(Intent.ACTION_WEB_SEARCH)
                intent.putExtra(SearchManager.QUERY, Web)
                startActivity(intent)
                true
            }

        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        actualizarMapa()

    }


    // Métodos del ciclo de vida para `MapView`
    override fun onStart() {
        super.onStart()
        mapView.onStart()
    }

    override fun onResume() {
        super.onResume()
        mapView.onResume()
    }

    override fun onPause() {
        super.onPause()
        mapView.onPause()
    }

    override fun onStop() {
        super.onStop()
        mapView.onStop()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        mapView.onDestroy()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mapView.onLowMemory()
    }


}