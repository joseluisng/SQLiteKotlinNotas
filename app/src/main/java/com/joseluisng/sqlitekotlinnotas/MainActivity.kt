package com.joseluisng.sqlitekotlinnotas

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.SearchView
import android.view.*
import android.widget.BaseAdapter
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.molde_notas.view.*

class MainActivity(var adapter : NotasAdapter? = null) : AppCompatActivity() {

    var listaDeNotas = ArrayList<Notas>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

       /* listaDeNotas.add(Notas(1, "Titulo", "Descripción"))
        listaDeNotas.add(Notas(2, "Titulo", "Descripción"))
        listaDeNotas.add(Notas(3, "Titulo", "Descripción"))

        adapter = NotasAdapter(this, listaDeNotas)
        listView.adapter = adapter */

        fab.setOnClickListener {
            val intent = Intent(this, AddActivity::class.java)
            startActivity(intent)
        }

        cargarQuery("%")

    }

    override fun onResume() {
        super.onResume()

        cargarQuery("%")
    }

    //SELECT ID, Titulo, Descripcion, FROM Notas Qhere (Titulo like ?) ORDER BY Titulo
    fun cargarQuery(titulo: String){
        var baseDatos = DBManager(this)
        val columnas = arrayOf("ID","Titulo","Descipcion")
        val selectionArgs = arrayOf(titulo)
        val cursor = baseDatos.query(columnas, "Titulo like ?", selectionArgs, "Titulo")

        listaDeNotas.clear()

        if(cursor.moveToFirst()){
            do{
                val ID = cursor.getInt(cursor.getColumnIndex("ID"))
                val titulo = cursor.getString(cursor.getColumnIndex("Titulo"))
                val descripcion = cursor.getString(cursor.getColumnIndex("Descripcion"))

                listaDeNotas.add(Notas(ID, titulo, descripcion))
            }while (cursor.moveToNext())
        }

        adapter = NotasAdapter(this, listaDeNotas)
        listView.adapter = adapter
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {

        menuInflater.inflate(R.menu.menu, menu)

        val buscar = menu!!.findItem(R.id.app_bar_search).actionView as SearchView
        val manejador = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        buscar.setSearchableInfo(manejador.getSearchableInfo(componentName))
        buscar.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(p0 : String?): Boolean {
                Toast.makeText(applicationContext, p0, Toast.LENGTH_SHORT).show()
                cargarQuery("%" + p0 + "%")
                return false
            }

            override fun onQueryTextChange(p0 : String?): Boolean {
                return false
            }
        })

        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item!!.itemId){
            R.id.menuAgregar ->{
                val intent = Intent(this, AddActivity::class.java)
                startActivity(intent)
            }
        }
        return super.onOptionsItemSelected(item)
    }


    inner class NotasAdapter(contexto : Context, var listaDeNotas: ArrayList<Notas>) : BaseAdapter() {

        var contexto: Context? = contexto

        override fun getView(p0: Int, view: View?, p2: ViewGroup?): View {
            //Hacemos un reciclado de vista
            var convertView : View ? = view
            if (convertView == null){
                convertView =  View.inflate(contexto, R.layout.molde_notas, null)
            }

            val nota = listaDeNotas[p0]

           // val inflater = contexto!!.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            val miVista = convertView!!   //inflater.inflate(R.layout.molde_notas, null)
            miVista.textViewTitulo.text = nota.titulo
            miVista.textViewContenido.text = nota.descripcion

            miVista.imageViewBorrar.setOnClickListener {
                val dbManager: DBManager = DBManager(this.contexto!!)
                val selectionArgs = arrayOf(nota.notaID.toString())

                dbManager.borrar("ID=?", selectionArgs)
                cargarQuery("%")

            }

            miVista.imageViewEditar.setOnClickListener {
                val intent = Intent(this@MainActivity, AddActivity::class.java)
                intent.putExtra("ID",  nota.notaID)
                intent.putExtra("titulo", nota.titulo)
                intent.putExtra("descrip", nota.descripcion)
                startActivity(intent)
            }
            return miVista

        }

        override fun getItem(p0: Int): Any {
            return listaDeNotas[p0]
        }

        override fun getItemId(p0: Int): Long {
            return p0.toLong()
        }

        override fun getCount(): Int {
            return listaDeNotas.size
        }

    }
}
