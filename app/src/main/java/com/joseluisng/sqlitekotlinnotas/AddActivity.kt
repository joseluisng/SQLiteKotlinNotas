package com.joseluisng.sqlitekotlinnotas

import android.content.ContentValues
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_add.*

class AddActivity : AppCompatActivity() {

    var id = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add)

        try {
            val bundle : Bundle = intent.extras
            id = bundle.getInt("ID",0)
            if (id != 0){
                editTextTitulo.setText(bundle.getString("titulo"))
                editTextDescripcion.setText(bundle.getString("descrip"))
            }
        }catch (e: Exception){}


    }

    fun btnAdd(view : View){
       val baseDatos = DBManager(this)

        val values = ContentValues()
        values.put("Titulo", editTextTitulo.text.toString())
        values.put("Descripcion", editTextDescripcion.text.toString())

        if (id == 0){

            val ID = baseDatos.insert(values)
            if (ID > 0){
                Toast.makeText(this, "Nota Agregada Correctamente", Toast.LENGTH_SHORT).show()
                finish()
            }else{
                Toast.makeText(this, "Lo siento, la nota no se pudo agregar", Toast.LENGTH_SHORT).show()
            }

        }else{

            val selectionArgs = arrayOf(id.toString())
            val ID = baseDatos.actualizar(values, "ID=?", selectionArgs)
            if (ID > 0){
                Toast.makeText(this, "Nota Agregada Correctamente", Toast.LENGTH_SHORT).show()
                finish()
            }else{
                Toast.makeText(this, "Lo siento, la nota no se pudo agregar", Toast.LENGTH_SHORT).show()
            }

        }



    }
}
