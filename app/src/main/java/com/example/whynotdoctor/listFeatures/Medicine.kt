package com.example.whynotdoctor

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
import com.example.whynotdoctor.listFeatures.LabAPI
import com.example.whynotdoctor.listFeatures.Labtest
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

data class Medicine(
    val name: String,
    val description: String,
    val price :Number,
    val category: String,
    val type: String,
    val onPrescription: Boolean,
    val _id: String,
    val __v : Number

)

interface MedicineAPI {
    @GET("medicine")
    fun getdata() : Call<List<Medicine>>
}

class MedicineViewModel : ViewModel() {
    val medicineList = MutableLiveData<List<Medicine>>()

    val _itemList: LiveData<List<Medicine>>
        get() = medicineList

    fun setItemList(_itemList: List<Medicine>) {
        medicineList.value = _itemList
    }


    fun fetchData() {

        val list1: List<Medicine> = listOf(
            Medicine(name = "Aryan", description = "Description 1",100,"Dentist","3",false,"123",234),
            Medicine(name = "Item 1", description = "Description 1",100,"Dentist","3",false,"123",234),
            Medicine(name = "Item 1", description = "Description 1",100,"Dentist","3",false,"123",234),
            Medicine(name = "Item 1", description = "Description 1",100,"Dentist","3",false,"123",234),

            )
        setItemList(list1)

    }
}


class MedicineListAdapter(private val medicines: List<Medicine>) :
    RecyclerView.Adapter<MedicineListAdapter.MedicineViewHolder>() {

    class MedicineViewHolder(medicineView: View) : RecyclerView.ViewHolder(medicineView) {
        val namemedicine = medicineView.findViewById<TextView>(R.id.medicinename)
        val pricemedicine = medicineView.findViewById<TextView>(R.id.medicineprice)
        val descriptionTextView1 = medicineView.findViewById<TextView>(R.id.medicinedesc)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MedicineViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.medicine_tile, parent, false)
        return MedicineViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MedicineViewHolder, position: Int) {
        val medicine = medicines[position]
        holder.namemedicine.text = medicine.name
        holder.pricemedicine.text="Rs. "+medicine.price.toString()
        holder.descriptionTextView1.text = medicine.description
        holder.itemView.setOnClickListener(){
            val context = holder.itemView.context
            val intent= Intent(context,Medicinedetail::class.java)
            intent.putExtra("med_Id",medicines[position]._id)
            context.startActivity(intent)
        }
    }

    override fun getItemCount() = medicines.size
}


class MedicineListActivity : AppCompatActivity() {

    private lateinit var viewModel: MedicineViewModel
    private lateinit var adapter: MedicineListAdapter

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.medicine_list)

        viewModel = ViewModelProvider(this).get(MedicineViewModel::class.java)

        lifecycleScope.launch {
            viewModel.fetchData()
        }

        val itemListRecyclerView = findViewById<RecyclerView>(R.id.medicine_recycler_list)
        viewModel.medicineList.observe(this, Observer {
            val TAG ="HELLO"
            Log.d(TAG, "CHALA KUCH TOH")

            adapter = MedicineListAdapter(it)
            itemListRecyclerView.adapter = adapter
        })

        val retrofit = Retrofit.Builder()
            .baseUrl(GlobalData.getInstance().URL_G.toString())
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val api = retrofit.create(MedicineAPI::class.java)

        val call: Call<List<Medicine>> = api.getdata()
        call.enqueue(object : Callback<List<Medicine>> {

            override fun onResponse(call: Call<List<Medicine>>, response: Response<List<Medicine>>) {

                val dataList = response.body()
                if (dataList != null) {
                    viewModel.setItemList(dataList)
                }
                Log.d("hi",dataList.toString())
            }

            override fun onFailure(call: Call<List<Medicine>>, t: Throwable) {

                Log.d("hi","badddd")
            }
        })


        itemListRecyclerView.layoutManager = LinearLayoutManager(this)

    }
}