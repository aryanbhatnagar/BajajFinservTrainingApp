package com.example.whynotdoctor

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
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
import retrofit2.http.Body
import retrofit2.http.GET


interface GetOrder {
    @GET("cart")
    fun getdata() : Call<List<Apt>>
}


class OrderViewModel : ViewModel() {
    val orderList = MutableLiveData<List<Apt>>()

    val _itemList: LiveData<List<Apt>>
        get() = orderList

    fun setItemList(_itemList: List<Apt>) {
        orderList.value = _itemList
    }


    fun fetchData() {

    }
}


class OrderListAdapter(var context : Context, val orders: List<Apt>) :
    RecyclerView.Adapter<OrderListAdapter.OrderViewHolder>() {


    class OrderViewHolder(orderView: View) : RecyclerView.ViewHolder(orderView) {
        val nameorder = orderView.findViewById<TextView>(R.id.order_name)
        val priceorder = orderView.findViewById<TextView>(R.id.order_price)
        val cat_order=orderView.findViewById<TextView>(R.id.order_cat)
        val mu_img = orderView.findViewById<ImageView>(R.id.order_img)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.ordertile, parent, false)
        return OrderViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: OrderViewHolder, position: Int) {
        val order = orders[position]
        holder.nameorder.text = order.name
        holder.priceorder.text = order.price
        holder.cat_order.text=order.description
        holder.mu_img.setBackgroundResource(R.drawable.medicine_confirm)


    }



    override fun getItemCount() = orders.size
}


class OrderListActivity : AppCompatActivity() {

    private lateinit var viewModel: OrderViewModel
    private lateinit var adapter: OrderListAdapter
    private lateinit var listApt :List<Apt>

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.order_page)

        val orderHeader = findViewById<ImageView>(R.id.orderheader)
        orderHeader.setBackgroundResource(R.drawable.doctor_header)

        viewModel = ViewModelProvider(this).get(OrderViewModel::class.java)
        lifecycleScope.launch {
            viewModel.fetchData()
        }

        val itemListRecyclerView = findViewById<RecyclerView>(R.id.order_recycler_view)
        viewModel.orderList.observe(this, Observer {
            adapter = OrderListAdapter(this,it)
            itemListRecyclerView.adapter = adapter
        })

        val retrofit = Retrofit.Builder()
            .baseUrl(GlobalData.getInstance().URL_G.toString())
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val api = retrofit.create(GetOrder::class.java)

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