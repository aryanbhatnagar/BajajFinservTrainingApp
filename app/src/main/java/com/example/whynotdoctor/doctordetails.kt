package com.example.whynotdoctor

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import java.util.*
import kotlin.collections.List

data class Apt(
    val name : String,
    val price : String,
    val description : String
)

interface GetDoctorAPI {
    @GET("doctor/{id}")
    fun getdata(@Path("id") id:String) : Call<Doctor>
}

interface PostApt {
    @POST("apt")
    fun getdata(@Body apt :Apt) : Call<Void>
}

class Doctordetails :AppCompatActivity() {

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        setContentView(R.layout.doctordetails_dialogue)
        val docname = findViewById<TextView>(R.id.doct_dial_name)
        val docdesc = findViewById<TextView>(R.id.doctor_desc_dial)
        val docspec = findViewById<TextView>(R.id.doct_dial_specialisation)
        val docfee = findViewById<TextView>(R.id.doctor_dial_fee)
        val docloc = findViewById<TextView>(R.id.doct_location)
        val apt_button = findViewById<Button>(R.id.book_appointment)
        val img = findViewById<ImageView>(R.id.detail_img)

        img.setBackgroundResource(R.drawable.doctor)


        val flightid = intent.getStringExtra("Doctor_Id")

        val retrofit = Retrofit.Builder()
            .baseUrl(GlobalData.getInstance().URL_G.toString())
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val api = retrofit.create(GetDoctorAPI::class.java)

        val call: Call<Doctor> = api.getdata(flightid.toString())
        call.enqueue(object : Callback<Doctor> {

            override fun onResponse(call: Call<Doctor>, response: Response<Doctor>) {

                val dataList = response.body()
                if (dataList != null) {
//                    val random = Random()
//                    val randomNumber = random.nextInt(dataList.size)
                    Log.d("hi",dataList.toString())
                    docname.text=dataList.name
                    docdesc.text=dataList.description
                    docspec.text=dataList.category
                    docfee.text=dataList.rating.toString()
                    docloc.text=dataList.area


                }

            }

            override fun onFailure(call: Call<Doctor>, t: Throwable) {
                Log.d("hi","badddd")
            }
        })

        apt_button.setOnClickListener(){
            val builder = AlertDialog.Builder(this)
            builder.setTitle("Book Appointment")
            builder.setMessage("Are you sure you want to book an appointment with ${docname.text.toString()}")
            builder.setPositiveButton("OK") { dialog, which ->

                val retrofit = Retrofit.Builder()
                    .baseUrl(GlobalData.getInstance().URL_G.toString())
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()

                val api = retrofit.create(PostApt::class.java)

                val book_apt = Apt(docname.text.toString(),docfee.text.toString(),docspec.text.toString())

                val call: Call<Void> = api.getdata(book_apt)
                call.enqueue(object : Callback<Void?> {
                    override fun onResponse(call: Call<Void?>, response: Response<Void?>) {
                        Log.d("Appointment","Successss")

                    }

                    override fun onFailure(call: Call<Void?>, t: Throwable) {

                    }
                })
                val intent = Intent(this, Confirmation::class.java)
                startActivity(intent)
                finish()

            }
            builder.setNegativeButton("Cancel") { dialog, which ->
                dialog.dismiss()
            }
            val dialog: AlertDialog = builder.create()
            dialog.show()

        }

    }

}