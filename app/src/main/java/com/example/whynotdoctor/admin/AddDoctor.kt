package com.example.whynotdoctor.admin

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.whynotdoctor.GlobalData
import com.example.whynotdoctor.R
import com.google.android.material.textfield.TextInputEditText
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.POST

data class DoctorAdd(
    val name: String,
    val description: String,
    val rating: String,
    val category: String,
    val year_experience: String,
    val area: String,
)


interface Doctorpost {
    @POST("doctor")
    fun getdata(@Body med:DoctorAdd) : Call<Void>
}

class AddDoctor : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.add_doctor)

        val docname = findViewById<TextInputEditText>(R.id.docname)
        val  doccat= findViewById<TextInputEditText>(R.id.doccat)
        val docexp = findViewById<TextInputEditText>(R.id.docexp)
        val docdesc = findViewById<TextInputEditText>(R.id.docdesc)
        val docfee = findViewById<TextInputEditText>(R.id.docprice)
        val docloc = findViewById<TextInputEditText>(R.id.docloc)

        val addbtn = findViewById<Button>(R.id.add_doc_btn)
        addbtn.setOnClickListener(){

            val postData_class : DoctorAdd = DoctorAdd(
                docname.text.toString(),
                docdesc.text.toString(),
                docfee.text.toString(),
                doccat.text.toString(),
                docexp.text.toString(),
                docloc.text.toString()

            )

            val retrofit = Retrofit.Builder()
                .baseUrl(GlobalData.getInstance().URL_G.toString())
                .addConverterFactory(GsonConverterFactory.create())
                .build()

            val api = retrofit.create(Doctorpost::class.java)
            val call: Call<Void> = api.getdata(postData_class)
            call.enqueue(object : Callback<Void?> {
                override fun onResponse(call: Call<Void?>, response: Response<Void?>) {
                    Log.d("doc","added sucess")
                }

                override fun onFailure(call: Call<Void?>, t: Throwable) {
                    Log.d("med","coulfnt")
                }
            })
            Toast.makeText(this,"Added Successfully",Toast.LENGTH_SHORT).show()
            startActivity(Intent(this, HomeAdmin::class.java))
            finish()

        }

    }
}