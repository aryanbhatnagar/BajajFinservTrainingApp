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
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface GetMedicineAPI {
    @GET("medicine/{id}")
    fun getdata(@Path("id") id:String) : Call<Medicine>
}

interface PostCart {
    @POST("cart")
    fun getdata(@Body apt :Apt) : Call<Void>
}


class Medicinedetail : AppCompatActivity() {

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        setContentView(R.layout.doctordetails_dialogue)
        val medicinename = findViewById<TextView>(R.id.doct_dial_name)
        val medicinedesc = findViewById<TextView>(R.id.doctor_desc_dial)
        val medicinecat = findViewById<TextView>(R.id.doct_dial_specialisation)
        val medicinefee = findViewById<TextView>(R.id.doctor_dial_fee)
        val homelab = findViewById<TextView>(R.id.doct_location)
        val onpresc_title = findViewById<TextView>(R.id.homeservice)
        val apt_button = findViewById<Button>(R.id.book_appointment)
        val img = findViewById<ImageView>(R.id.detail_img)

        img.setBackgroundResource(R.drawable.medicine)
        apt_button.text="BUY MEDICINE"
        onpresc_title.text="On Prescription"

        val flightid = intent.getStringExtra("med_Id")

        val retrofit = Retrofit.Builder()
            .baseUrl(GlobalData.getInstance().URL_G.toString())
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val api = retrofit.create(GetMedicineAPI::class.java)

        val call: Call<Medicine> = api.getdata(flightid.toString())
        call.enqueue(object : Callback<Medicine> {

            override fun onResponse(call: Call<Medicine>, response: Response<Medicine>) {

                val dataList = response.body()
                if (dataList != null) {

                    Log.d("hi",dataList.toString())
                    medicinename.text=dataList.name
                    medicinedesc.text=dataList.description
                    medicinecat.text=dataList.category
                    medicinefee.text=dataList.price.toString()
                    if(dataList.onPrescription)
                        homelab.text="Yes"
                    else
                        homelab.text="No"

                }

            }

            override fun onFailure(call: Call<Medicine>, t: Throwable) {
                Log.d("hi","badddd")
            }
        })

        apt_button.setOnClickListener(){
            val builder = AlertDialog.Builder(this)
            builder.setTitle("Buy Medicine")
            builder.setMessage("Are you sure you want to buy ${medicinename.text.toString()}")
            builder.setPositiveButton("OK") { dialog, which ->

                val retrofit = Retrofit.Builder()
                    .baseUrl(GlobalData.getInstance().URL_G.toString())
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()

                val api = retrofit.create(PostCart::class.java)

                val book_apt = Apt(medicinename.text.toString(),medicinefee.text.toString(),medicinecat.text.toString())

                val call: Call<Void> = api.getdata(book_apt)
                call.enqueue(object : Callback<Void?> {
                    override fun onResponse(call: Call<Void?>, response: Response<Void?>) {
                        Log.d("Appointment","Successss")

                    }

                    override fun onFailure(call: Call<Void?>, t: Throwable) {

                    }
                })
                val intent = Intent(this, ConfirmMedicine::class.java)
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