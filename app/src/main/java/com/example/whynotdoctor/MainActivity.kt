package com.example.whynotdoctor

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import com.example.whynotdoctor.listFeatures.LabtestListActivity

class MainActivity : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.home_screen)

        val update_medical_btn :Button = findViewById(R.id.update_info)
        update_medical_btn.setOnClickListener(){
            startActivity(Intent(this, UpdateInfo::class.java))
        }

        val medicine_btn : View = findViewById(R.id.medicine_click)
        medicine_btn.setOnClickListener(){
            startActivity(Intent(this, MedicineListActivity::class.java))
        }

        val labtest_btn : View = findViewById(R.id.labTest_click)
        labtest_btn.setOnClickListener(){
            startActivity(Intent(this, LabtestListActivity::class.java))
        }

        val doctor_btn : View = findViewById(R.id.doctor_click)
        doctor_btn.setOnClickListener(){
            startActivity(Intent(this, DoctorListActivity::class.java))
        }

        val order_btn : View = findViewById(R.id.order_click)
        order_btn.setOnClickListener(){
            startActivity(Intent(this, OrderListActivity::class.java))
        }

        val apt_btn : View = findViewById(R.id.apt_click)
        apt_btn.setOnClickListener(){
            startActivity(Intent(this, AptListActivity::class.java))
        }

        val logout_btn = findViewById<Button>(R.id.logout1)
        logout_btn.setOnClickListener(){
            startActivity(Intent(this, LoginSignupActivity::class.java))
            finish()
        }
    }
}