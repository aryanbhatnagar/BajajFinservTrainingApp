package com.example.whynotdoctor

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity

class ConfirmMedicine : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        Log.d("confirmatuion", "confirmation run")
        setContentView(R.layout.confirm_screen)

    }
}