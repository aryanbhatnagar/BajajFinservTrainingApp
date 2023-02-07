package com.example.whynotdoctor

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
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
import retrofit2.http.GET

interface GetApt {
    @GET("apt")
    fun getdata() : Call<List<Apt>>
}

class AptViewModel : ViewModel() {
    val aptList = MutableLiveData<List<Apt>>()

    val _itemList: LiveData<List<Apt>>
        get() = aptList

    fun setItemList(_itemList: List<Apt>) {
        aptList.value = _itemList
    }


    fun fetchData() {

    }
}


class AptListAdapter(var context : Context, val apts: List<Apt>) :
    RecyclerView.Adapter<AptListAdapter.AptViewHolder>() {


    class AptViewHolder(aptView: View) : RecyclerView.ViewHolder(aptView) {
        val nameorder = aptView.findViewById<TextView>(R.id.order_name)
        val priceorder = aptView.findViewById<TextView>(R.id.order_price)
        val cat_order=aptView.findViewById<TextView>(R.id.order_cat)
        val mu_img = aptView.findViewById<ImageView>(R.id.order_img)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AptViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.ordertile, parent, false)
        return AptViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: AptViewHolder, position: Int) {
        val apt = apts[position]
        holder.nameorder.text = apt.name
        holder.priceorder.text = apt.price
        holder.cat_order.text=apt.description
        holder.mu_img.setBackgroundResource(R.drawable.splashscreen)

    }

    override fun getItemCount() = apts.size
}


class AptListActivity : AppCompatActivity() {

    private lateinit var viewModel: AptViewModel
    private lateinit var adapter: AptListAdapter
    private lateinit var listApt :List<Apt>

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.order_page)

        val orderHeader = findViewById<ImageView>(R.id.orderheader)
        orderHeader.setBackgroundResource(R.drawable.intro_medicine)

        val orderTitle =findViewById<TextView>(R.id.order_title)
        orderTitle.text="MY APPOINTMENTS"

        viewModel = ViewModelProvider(this).get(AptViewModel::class.java)
        lifecycleScope.launch {
            viewModel.fetchData()
        }

        val itemListRecyclerView = findViewById<RecyclerView>(R.id.order_recycler_view)
        viewModel.aptList.observe(this, Observer {
            adapter = AptListAdapter(this,it)
            itemListRecyclerView.adapter = adapter
        })

        val retrofit = Retrofit.Builder()
            .baseUrl(GlobalData.getInstance().URL_G.toString())
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val api = retrofit.create(GetApt::class.java)

        val call: Call<List<Apt>> = api.getdata()
        call.enqueue(object : Callback<List<Apt>> {

            override fun onResponse(call: Call<List<Apt>>, response: Response<List<Apt>>) {

                val dataList = response.body()
                if (dataList != null) {
                    viewModel.setItemList(dataList)
                }
                Log.d("hi",dataList.toString())
            }

            override fun onFailure(call: Call<List<Apt>>, t: Throwable) {

                Log.d("hi","badddd")
            }
        })

        itemListRecyclerView.layoutManager = LinearLayoutManager(this)

    }

}