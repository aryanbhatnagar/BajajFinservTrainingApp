package com.example.whynotdoctor

import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class UpdateInfo : AppCompatActivity(){

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.medical_info)

        val update_medical_btn : Button = findViewById(R.id.update_info)
        update_medical_btn.setOnClickListener(){

        }
    }

}