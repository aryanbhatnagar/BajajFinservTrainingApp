package com.example.whynotdoctor.admin

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.whynotdoctor.*
import com.example.whynotdoctor.listFeatures.LabtestListActivity

class HomeAdmin : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.admin_home)

        val addmed_btn = findViewById<Button>(R.id.add_medicine)
        addmed_btn.setOnClickListener(){
            startActivity(Intent(this, AddMedicine::class.java))
        }

        val adddoc_btn = findViewById<Button>(R.id.add_doctor)
        adddoc_btn.setOnClickListener(){
            startActivity(Intent(this, AddDoctor::class.java))
        }

        val deldoc_btn = findViewById<Button>(R.id.del_doctor)
        deldoc_btn.setOnClickListener(){
            startActivity(Intent(this, DeleteDoctor::class.java))
        }


        val addlb_btn = findViewById<Button>(R.id.add_labtest)
        addlb_btn.setOnClickListener(){
            startActivity(Intent(this, AddLabtest::class.java))
        }

        val logout_btn = findViewById<Button>(R.id.logout)
        logout_btn.setOnClickListener(){
            startActivity(Intent(this, LoginSignupActivity::class.java))
            finish()
        }

    }
}