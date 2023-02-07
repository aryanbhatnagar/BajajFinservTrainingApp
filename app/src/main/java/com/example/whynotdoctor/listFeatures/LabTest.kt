package com.example.whynotdoctor.listFeatures

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.*
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.whynotdoctor.*
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

data class Labtest(
    val name: String,
    val description: String,
    val type :String,
    val lab : String,
    val home_service: Boolean,
    val price: Number,
    val _id: String,
    val __v : Number

)

interface LabAPI {
    @GET("lab")
    fun getdata() : Call<List<Labtest>>
}

class LatestViewModel : ViewModel() {
    val labtestList = MutableLiveData<List<Labtest>>()

    val _itemList: LiveData<List<Labtest>>
        get() = labtestList

    fun setItemList(_itemList: List<Labtest>) {
        labtestList.value = _itemList
    }


    fun fetchData() {


    }
}


class LabtestListAdapter(private val labtests: List<Labtest>) :
    RecyclerView.Adapter<LabtestListAdapter.LabtestHolder>() {

    class LabtestHolder(LabTestView: View) : RecyclerView.ViewHolder(LabTestView) {
        val Labtestname = LabTestView.findViewById<TextView>(R.id.labtest_name)
        val labtestprice = LabTestView.findViewById<TextView>(R.id.labtest_price)
        val labtestdesc = LabTestView.findViewById<TextView>(R.id.labtest_desc)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LabtestHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.labtest_tile, parent, false)
        return LabtestHolder(itemView)
    }

    override fun onBindViewHolder(holder: LabtestHolder, position: Int) {
        val labtest = labtests[position]
        holder.Labtestname.text = labtest.name
        holder.labtestdesc.text = labtest.description
        holder.labtestprice.text= "Rs. "+labtest.price
        holder.itemView.setOnClickListener(){
            val context = holder.itemView.context
            val intent= Intent(context, Labtestdetails::class.java)
            intent.putExtra("lab_Id",labtests[position]._id)
            context.startActivity(intent)
        }
    }

    override fun getItemCount() = labtests.size
}


class LabtestListActivity : AppCompatActivity() {

    private lateinit var viewModel: LatestViewModel
    private lateinit var adapter: LabtestListAdapter

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.labtest_list)

        viewModel = ViewModelProvider(this).get(LatestViewModel::class.java)
        lifecycleScope.launch {
            viewModel.fetchData()
        }

        val itemListRecyclerView = findViewById<RecyclerView>(R.id.labtest_recycler_list)
        viewModel.labtestList.observe(this, Observer {
            val TAG ="HELLO"
            Log.d(TAG, "CHALA KUCH TOH")

            adapter = LabtestListAdapter(it)
            itemListRecyclerView.adapter = adapter
        })

        val retrofit = Retrofit.Builder()
            .baseUrl(GlobalData.getInstance().URL_G.toString())
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val api = retrofit.create(LabAPI::class.java)

        val call: Call<List<Labtest>> = api.getdata()
        call.enqueue(object : Callback<List<Labtest>> {

            override fun onResponse(call: Call<List<Labtest>>, response: Response<List<Labtest>>) {

                val dataList = response.body()
                if (dataList != null) {
                    viewModel.setItemList(dataList)
                }
                Log.d("hi",dataList.toString())
            }

            override fun onFailure(call: Call<List<Labtest>>, t: Throwable) {

                Log.d("hi","badddd")
            }
        })


//        viewModel.setItemList(listOf(
//            Labtest(name = "Item 1", description = "Description 1","3","Dentist",3,"kharadi","123","123",234),
//            Labtest(name = "Item 1", description = "Description 1","3","Dentist",3,"kharadi","123","123",234),
//            Labtest(name = "Item 1", description = "Description 1","3","Dentist",3,"kharadi","123","123",234),
//
//            ))


        itemListRecyclerView.layoutManager = LinearLayoutManager(this)

    }
}