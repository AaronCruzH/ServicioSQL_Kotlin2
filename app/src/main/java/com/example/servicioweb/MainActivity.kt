package com.example.servicioweb

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import org.json.JSONArray
import org.json.JSONObject

class MainActivity : AppCompatActivity() {
    var datos = mutableListOf<String>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //cola de peticion
        val cola = Volley.newRequestQueue(this@MainActivity)
        val lvUsuario = findViewById<ListView>(R.id.LvUsuario)

        lvUsuario.setOnItemClickListener { parent, view, position, id ->
            val builder = AlertDialog.Builder(this@MainActivity)
            builder.setTitle("Confirmacion de eliminacion")
            builder.setMessage("¿Estas seguro de eliminar el usuario?")
            builder.setNegativeButton("Si"){dialog,_ ->
                ////PEGAR AQUI
                val correo = datos[position]
                //"http:localhost/usuario/"+correo
                val url = "http:localhost:3000/usuario/$correo"
                val metodo = Request.Method.DELETE

                val todoBien = Response.Listener<JSONObject> { response ->
                    var mensaje = "Ops!, Algo paso:"
                    if(response.getInt("insertID")==0){
                        mensaje = "El registro fue borrado correctamente"
                    }
                    Toast.makeText(this@MainActivity,
                        mensaje,
                        Toast.LENGTH_SHORT).show()
                }

                val algoMal = Response.ErrorListener { error ->

                }
                val request = JsonObjectRequest(metodo,url,null,todoBien,algoMal)
                cola.add(request)
            }
            builder.setPositiveButton("No"){dialog,_ ->
                dialog.dismiss()
            }
        }


        //request
        val url = "http://192.168.1.127:3000/usuario"
        val metodo = Request.Method.GET
        val todobien = Response.Listener<JSONArray> { response ->
            datos = mutableListOf<String>()
            var objeto: JSONObject
            for (x in 0..response.length() - 1) {
                objeto = response.getJSONObject(x)
                datos.add(objeto.getString("correo"))
            }
            val adaptador = ArrayAdapter(
                this@MainActivity,
                android.R.layout.simple_list_item_1,
                datos
            )
            lvUsuario.adapter = adaptador
        }
        val algoMal = Response.ErrorListener { error ->
            Log.e("algoMal", error.toString())
        }
        val request = JsonArrayRequest(metodo, url, null, todobien, algoMal)
        cola.add(request)

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        //¿Que se va a cargar?, ¿a donde?
        menuInflater.inflate(
            R.menu.menu_main,
            menu
        )//accion de leer un archivo xml y lo pegara a la parte grafica
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        //verificar que elemento se selecciono
        //switch
        when (item.itemId) {
            R.id.mnuAyuda -> {
                //Toast
                //contexto(Componente que lo ejecuta)
                //2. Texto que despliega
                //3. Duración del Toast (SHORT 2, lONG 5)
                /*Toast.makeText(
                    this@MainActivity,
                    "Presionaste ayuda!", //R.string.txtAyuda con la carpeta de strings
                    Toast.LENGTH_SHORT
                ).show()*/
                val intent = Intent(this@MainActivity, formulario::class.java)
                startActivity(intent)

            }


        }
        return super.onOptionsItemSelected(item)
    }


}