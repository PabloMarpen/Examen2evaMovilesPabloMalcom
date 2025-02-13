package com.example.examen2evamovilespablomalcom


import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.EditText
import android.widget.RatingBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

// Define la clase DiscosAdapter, que extiende RecyclerView.Adapter y especifica MyViewHolder como su ViewHolder.
// esto es para los restaurantes que se muestra en el listado
// para inflar la lista
class RestauranteAdapter(private val context: Context, private val listaBares: List<Bar>) : BaseAdapter() {

    // Devuelve el número total de elementos en la lista.
    override fun getCount(): Int = listaBares.size

    // Devuelve el elemento de la lista en la posición especificada.
    override fun getItem(position: Int): Any = listaBares[position]

    // Devuelve el identificador único del elemento en la posición especificada.
    override fun getItemId(position: Int): Long = position.toLong()

    // Método para crear o reutilizar una vista y asignar los datos del bar en la lista.
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        var view = convertView
        if (view == null) {
            val inflater =
                context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            view = inflater.inflate(R.layout.bar_item, parent, false)
        }


        val bar = getItem(position) as Bar

        val nombreRestaurante = view?.findViewById<TextView>(R.id.textViewNombre)
        val textViewWeb = view?.findViewById<TextView>(R.id.textViewWeb)


        nombreRestaurante?.text = bar.NombreBar
        textViewWeb?.text = bar.Web


        return view!!
    }
}