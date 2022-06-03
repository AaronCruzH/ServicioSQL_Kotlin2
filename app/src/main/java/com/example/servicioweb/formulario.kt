package com.example.servicioweb

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import org.json.JSONObject

class formulario : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_formulario)

        val edtNombre= findViewById<EditText>(R.id.edtNombre)
        val edtPaterno= findViewById<EditText>(R.id.edtPaterno)
        val edtMaterno= findViewById<EditText>(R.id.edtMaterno)
        val edtNacimiento= findViewById<EditText>(R.id.edtNacimiento)
        val btnGuadar= findViewById<Button>(R.id.btnGuardar)
        //cola de peticion
        val cola = Volley.newRequestQueue(this@formulario)

        btnGuadar.setOnClickListener {
            val metodo= Request.Method.POST
            //request
            val url= "http://192.168.1.127:3000/persona"
            val body= JSONObject()
            body.put("nombre", edtNombre.text.toString())
            body.put("paterno", edtPaterno.text.toString())
            body.put("materno", edtMaterno.text.toString())
            body.put("nacimiento", edtNacimiento.text.toString())
            edtNombre.text.clear()
            edtMaterno.text.clear()
            edtPaterno.text.clear()
            edtNacimiento.text.clear()

            val todoBien= Response.Listener<JSONObject>{response ->
                var mensaje= "Ops! Algo Paso!"
                if (response.getInt("affectedRows")>0 &&
                        response.getInt("insertId")>0){
                            mensaje= "el Registro se insertó correctamente"
                    }
                Toast.makeText(this@formulario, mensaje, Toast.LENGTH_SHORT)
                    .show()
            }
            val algoMal= Response.ErrorListener { error ->

            }
            val request= JsonObjectRequest(metodo, url,body,todoBien,algoMal)
            cola.add(request)

        }

    }
}