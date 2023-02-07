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

data class LabtestAdd(
    val name: String,
    val type: String,
    val lab: String,
    val home_service: String,
    val price: String,
    val description: String,
    val result_time: String,
)


interface Labtestpost {
    @POST("lab")
    fun getdata(@Body med:LabtestAdd) : Call<Void>
}

class AddLabtest : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.add_labtest)

        val labtestname = findViewById<TextInputEditText>(R.id.labtestname)
        val  labtesttype= findViewById<TextInputEditText>(R.id.labtesttype)
        val labname = findViewById<TextInputEditText>(R.id.labname)
        val labtestdesc = findViewById<TextInputEditText>(R.id.labtestdesc)
        val labtestprice = findViewById<TextInputEditText>(R.id.labtestprice)
        val labtesthome = findViewById<TextInputEditText>(R.id.labtesthome)
        val labtestresult = findViewById<TextInputEditText>(R.id.labtestresult)

        val addbtn = findViewById<Button>(R.id.add_labtest_btn)
        addbtn.setOnClickListener(){

            val postData_class : LabtestAdd = LabtestAdd(labtestname.text.toString(),labtesttype.text.toString(),labname.text.toString(),labtesthome.text.toString(),labtestprice.text.toString(),labtestdesc.text.toString(),labtestresult.text.toString())
            val retrofit = Retrofit.Builder()
                .baseUrl(GlobalData.getInstance().URL_G.toString())
                .addConverterFactory(GsonConverterFactory.create())
                .build()

            val api = retrofit.create(Labtestpost::class.java)
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