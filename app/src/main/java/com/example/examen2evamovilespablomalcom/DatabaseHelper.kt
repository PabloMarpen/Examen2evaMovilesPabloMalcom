package com.example.examen2evamovilespablomalcom


import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteException
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log

class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {


    companion object {
        private const val DATABASE_NAME = "BaresDatabase"
        private const val DATABASE_VERSION = 1
        private const val TABLE_BARES = "bares"
        private const val KEY_ID = "id"
        private const val KEY_NOMBRE = "nombre"
        private const val KEY_DIRECCION = "direccion"
        private const val KEY_VALORACION = "valoracion"
        private const val KEY_LATITUD = "latitud"
        private const val KEY_LONGITUD = "longitud"
        private const val KEY_WEB = "web"
    }


    override fun onCreate(db: SQLiteDatabase) {
        val createBaresTable = ("CREATE TABLE $TABLE_BARES ("
                + "$KEY_ID INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "$KEY_NOMBRE TEXT NOT NULL, "
                + "$KEY_DIRECCION TEXT NOT NULL, "
                + "$KEY_VALORACION INTEGER NOT NULL CHECK($KEY_VALORACION BETWEEN 0 AND 5), "
                + "$KEY_LATITUD REAL NOT NULL, "
                + "$KEY_LONGITUD REAL NOT NULL, "
                + "$KEY_WEB TEXT)")
        db.execSQL(createBaresTable)

    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_BARES")
        onCreate(db)
    }

    fun getAllBares(): ArrayList<Bar> {
        val listaBares = ArrayList<Bar>()
        val selectQuery = "SELECT  * FROM $TABLE_BARES"

        val db = this.readableDatabase
        var cursor: Cursor? = null
        try {
            cursor = db.rawQuery(selectQuery, null)
        } catch (e: SQLiteException) {
            return ArrayList()
        }


        var id : Int
        var NombreBar : String
        var Direccion : String
        var Valoracion : Int
        var Latitud : Double
        var Longitud : Double
        var Web : String


        if (cursor.moveToFirst()) {
            do {
                val idIndex = cursor.getColumnIndex(KEY_ID)
                val nombreIndex = cursor.getColumnIndex(KEY_NOMBRE)
                val direccionIndex = cursor.getColumnIndex(KEY_DIRECCION)
                val valoracionIndex = cursor.getColumnIndex(KEY_VALORACION)
                val latitudIndex = cursor.getColumnIndex(KEY_LATITUD)
                val longitudIndex = cursor.getColumnIndex(KEY_LONGITUD)
                val webIndex = cursor.getColumnIndex(KEY_WEB)

                if (idIndex != -1 && nombreIndex != -1 && direccionIndex != -1
                    && valoracionIndex != -1  && latitudIndex != -1  && longitudIndex != -1  && webIndex != -1) {
                    id = cursor.getInt(idIndex)

                    // Verificar si los valores son NULL antes de asignarlos
                    NombreBar = cursor.getString(nombreIndex) ?: "Sin nombre"
                    Direccion = cursor.getString(direccionIndex) ?: "Sin Dirección"
                    Valoracion = if (!cursor.isNull(valoracionIndex)) cursor.getInt(valoracionIndex) else 0
                    Latitud = if (!cursor.isNull(latitudIndex)) cursor.getDouble(latitudIndex) else 0.0
                    Longitud = if (!cursor.isNull(longitudIndex)) cursor.getDouble(longitudIndex) else 0.0
                    Web = cursor.getString(webIndex) ?: "Sin Web"


                    val bar = Bar(id = id, NombreBar = NombreBar, Direccion = Direccion, Valoracion = Valoracion, Latitud = Latitud, Longitud = Longitud, Web = Web)
                    listaBares.add(bar)
                }
            } while (cursor.moveToNext())
        }


        cursor.close()
        return listaBares
    }

    fun updateBar(bar: Bar): Int {
        val db = this.writableDatabase
        val contentValues = ContentValues().apply {
            put(KEY_NOMBRE, bar.NombreBar)
            put(KEY_DIRECCION, bar.Direccion)
            put(KEY_VALORACION, bar.Valoracion)
            put(KEY_LATITUD, bar.Latitud)
            put(KEY_LONGITUD, bar.Longitud)
            put(KEY_WEB, bar.Web)
        }
        return db.update(TABLE_BARES, contentValues, "$KEY_ID = ?", arrayOf(bar.id.toString())).also { db.close() }
    }

    fun deleteBar(bar: Bar): Int {
        val db = this.writableDatabase
        return db.delete(TABLE_BARES, "$KEY_ID = ?", arrayOf(bar.id.toString())).also { db.close() }
    }


    fun addBar(bar: Bar): Long {
        val db = this.writableDatabase
        val contentValues = ContentValues().apply {
            put(KEY_NOMBRE, bar.NombreBar)
            put(KEY_DIRECCION, bar.Direccion)
            put(KEY_VALORACION, bar.Valoracion)
            put(KEY_LATITUD, bar.Latitud)
            put(KEY_LONGITUD, bar.Longitud)
            put(KEY_WEB, bar.Web)
        }
        return db.insert(TABLE_BARES, null, contentValues).also { db.close() }
    }

}