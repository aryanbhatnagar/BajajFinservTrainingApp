package com.example.whynotdoctor

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.whynotdoctor.listFeatures.Labtest
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path



interface GetLabAPI {
    @GET("lab/{id}")
    fun getdata(@Path("id") id:String) : Call<Labtest>
}




class Labtestdetails : AppCompatActivity() {

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        setContentView(R.layout.doctordetails_dialogue)
        val Labtestname = findViewById<TextView>(R.id.doct_dial_name)
        val Labtestdesc = findViewById<TextView>(R.id.doctor_desc_dial)
        val labname = findViewById<TextView>(R.id.doct_dial_specialisation)
        val labfee = findViewById<TextView>(R.id.doctor_dial_fee)
        val homelab = findViewById<TextView>(R.id.doct_location)
        val homelab_title = findViewById<TextView>(R.id.homeservice)
        val apt_button = findViewById<Button>(R.id.book_appointment)
        val img = findViewById<ImageView>(R.id.detail_img)

        img.setBackgroundResource(R.drawable.labtest)

        homelab_title.text="Home Service Available"

        val flightid = intent.getStringExtra("lab_Id")

        val retrofit = Retrofit.Builder()
            .baseUrl(GlobalData.getInstance().URL_G.toString())
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val api = retrofit.create(GetLabAPI::class.java)

        val call: Call<Labtest> = api.getdata(flightid.toString())
        call.enqueue(object : Callback<Labtest> {

            override fun onResponse(call: Call<Labtest>, response: Response<Labtest>) {

                val dataList = response.body()
                if (dataList != null) {

                    Log.d("hi",dataList.toString())
                    Labtestname.text=dataList.name
                    Labtestdesc.text=dataList.description
                    labname.text=dataList.lab
                    labfee.text=dataList.price.toString()
                    if(dataList.home_service)
                        homelab.text="Available"
                    else
                        homelab.text="Not Available"

                }

            }

            override fun onFailure(call: Call<Labtest>, t: Throwable) {
                Log.d("hi","badddd")
            }
        })

        apt_button.setOnClickListener(){
            val builder = AlertDialog.Builder(this)
            builder.setTitle("Book Appointment")
            builder.setMessage("Are you sure you want to book a ${Labtestname.text.toString()} appointment at ${labname.text.toString()}")
            builder.setPositiveButton("OK") { dialog, which ->

                val retrofit = Retrofit.Builder()
                    .baseUrl(GlobalData.getInstance().URL_G.toString())
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()

                val api = retrofit.create(PostApt::class.java)

                val book_apt = Apt(Labtestname.text.toString(),labfee.text.toString(),labname.text.toString())

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


