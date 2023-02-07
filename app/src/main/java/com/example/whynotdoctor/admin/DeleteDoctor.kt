package com.example.whynotdoctor.admin

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat.startActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.whynotdoctor.*
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class DelDoctorListAdapter(var context : Context, val doctors: List<Doctor>) :
    RecyclerView.Adapter<DelDoctorListAdapter.DelDoctorViewHolder>() {


    class DelDoctorViewHolder(doctorView: View) : RecyclerView.ViewHolder(doctorView) {
        val namedoctor = doctorView.findViewById<TextView>(R.id.doctorname)
        val doctorcategory = doctorView.findViewById<TextView>(R.id.doctorcategory)
        val doctexp=doctorView.findViewById<TextView>(R.id.doctorfee)
        val doctarea=doctorView.findViewById<TextView>(R.id.doctorarea)




    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DelDoctorViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.del_doc_tile, parent, false)
        return DelDoctorViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: DelDoctorViewHolder, position: Int) {
        val doctor = doctors[position]
        holder.namedoctor.text = doctor.name
        holder.doctorcategory.text = doctor.category
        holder.doctexp.text=doctor.year_experience.toString()+" yrs"
        holder.doctarea.text=doctor.area
        holder.itemView.setOnClickListener(){
            val retrofit = Retrofit.Builder()
                .baseUrl(GlobalData.getInstance().URL_G.toString())
                .addConverterFactory(GsonConverterFactory.create())
                .build()

            val api = retrofit.create(DoctorAPI::class.java)

            val call: Call<Void> = api.deldat(doctor._id)
            call.enqueue(object : Callback<Void?> {
                override fun onResponse(call: Call<Void?>, response: Response<Void?>) {

                }

                override fun onFailure(call: Call<Void?>, t: Throwable) {

                }
            })

            val context = holder.itemView.context
            Toast.makeText(context,"Deleted Successfully",Toast.LENGTH_SHORT).show()
            val intent=Intent(context,HomeAdmin::class.java)
            context.startActivity(intent)


        }

    }



    override fun getItemCount() = doctors.size
}


class DeleteDoctor : AppCompatActivity() {

    private lateinit var viewModel: DoctorViewModel
    private lateinit var adapter: DelDoctorListAdapter
    private lateinit var listdoctor :List<Doctor>

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.doctor_list)

        viewModel = ViewModelProvider(this).get(DoctorViewModel::class.java)
        lifecycleScope.launch {
            viewModel.fetchData()
        }

        val itemListRecyclerView = findViewById<RecyclerView>(R.id.doctor_recycler_list)
        viewModel.doctorList.observe(this, Observer {
            adapter = DelDoctorListAdapter(this,it)
            itemListRecyclerView.adapter = adapter

        })


        val retrofit = Retrofit.Builder()
            .baseUrl(GlobalData.getInstance().URL_G.toString())
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val api = retrofit.create(DoctorAPI::class.java)

        val call: Call<List<Doctor>> = api.getdata()
        call.enqueue(object : Callback<List<Doctor>> {

            override fun onResponse(call: Call<List<Doctor>>, response: Response<List<Doctor>>) {

                val dataList = response.body()
                if (dataList != null) {
                    viewModel.setItemList(dataList)
                }
                Log.d("hi",dataList.toString())
            }

            override fun onFailure(call: Call<List<Doctor>>, t: Throwable) {

                Log.d("hi","badddd")
            }
        })


        itemListRecyclerView.layoutManager = LinearLayoutManager(this)

    }
}