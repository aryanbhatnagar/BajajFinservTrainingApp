package com.example.whynotdoctor

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.*
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Path


data class Doctor(
    val name: String,
    val description: String,
    val rating :String,
    val category: String,
    val year_experience: Number,
    val area: String,
    val _id: String,
    val creation_date: String,
    val __v : Number

)

interface DoctorAPI {
    @GET("doctor")
    fun getdata() : Call<List<Doctor>>

    @DELETE("doctor/{id}")
    fun deldat(@Path("id") id:String) : Call<Void>

}


class DoctorViewModel : ViewModel() {
    val doctorList = MutableLiveData<List<Doctor>>()

    val _itemList: LiveData<List<Doctor>>
        get() = doctorList

    fun setItemList(_itemList: List<Doctor>) {
        doctorList.value = _itemList
    }


    fun fetchData() {



    }
}


class DoctorListAdapter(var context : Context,val doctors: List<Doctor>) :
    RecyclerView.Adapter<DoctorListAdapter.DoctorViewHolder>() {


    class DoctorViewHolder(doctorView: View) : RecyclerView.ViewHolder(doctorView) {
        val namedoctor = doctorView.findViewById<TextView>(R.id.doctorname)
        val doctorcategory = doctorView.findViewById<TextView>(R.id.doctorcategory)
        val doctexp=doctorView.findViewById<TextView>(R.id.doctorfee)
        val doctarea=doctorView.findViewById<TextView>(R.id.doctorarea)




    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DoctorViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.doctor_tile, parent, false)
        return DoctorViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: DoctorViewHolder, position: Int) {
        val doctor = doctors[position]
        holder.namedoctor.text = doctor.name
        holder.doctorcategory.text = doctor.category
        holder.doctexp.text=doctor.year_experience.toString()+" yrs"
        holder.doctarea.text=doctor.area
        holder.itemView.setOnClickListener(){
            val context = holder.itemView.context
            val intent=Intent(context,Doctordetails::class.java)
            intent.putExtra("Doctor_Id",doctors[position]._id)
            context.startActivity(intent)
        }

    }



    override fun getItemCount() = doctors.size
}


class DoctorListActivity : AppCompatActivity() {

    private lateinit var viewModel: DoctorViewModel
    private lateinit var adapter: DoctorListAdapter
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
            adapter = DoctorListAdapter(this,it)
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





