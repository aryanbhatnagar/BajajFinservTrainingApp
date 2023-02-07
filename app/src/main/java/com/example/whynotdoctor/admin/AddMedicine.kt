package com.example.whynotdoctor.admin

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.whynotdoctor.GlobalData
import com.example.whynotdoctor.PostUserAPI
import com.example.whynotdoctor.R
import com.google.android.material.textfield.TextInputEditText
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.POST

data class MedicineAdd(
    val name: String,
    val description: String,
    val price: String,
    val category: String,
    val type: String,
)

interface Medicinepost {
    @POST("medicine")
    fun getdata(@Body med:MedicineAdd) : Call<Void>
}

class AddMedicine : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.add_medicine)

        val medname = findViewById<TextInputEditText>(R.id.medname)
        val medType = findViewById<TextInputEditText>(R.id.medtype)
        val medcat = findViewById<TextInputEditText>(R.id.medcat)
        val meddesc = findViewById<TextInputEditText>(R.id.meddesc)
        val medfee = findViewById<TextInputEditText>(R.id.medprice)

        val addbtn = findViewById<Button>(R.id.add_med_btn)
        addbtn.setOnClickListener(){
            val postData_class : MedicineAdd = MedicineAdd(
                medname.text.toString(),
                meddesc.text.toString(),
                medfee.text.toString(),
                medcat.text.toString(),
                medType.text.toString()
            )

            val retrofit = Retrofit.Builder()
                .baseUrl(GlobalData.getInstance().URL_G.toString())
                .addConverterFactory(GsonConverterFactory.create())
                .build()

            val api = retrofit.create(Medicinepost::class.java)
            val call: Call<Void> = api.getdata(postData_class)
            call.enqueue(object : Callback<Void?> {
                override fun onResponse(call: Call<Void?>, response: Response<Void?>) {
                   Log.d("med","added sucess")
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