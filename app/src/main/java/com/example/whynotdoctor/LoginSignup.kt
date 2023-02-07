package com.example.whynotdoctor


import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast
import android.widget.ToggleButton
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentTransaction


class LoginSignupActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login_signup)

        val fragmentTransaction: FragmentTransaction =
            supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.fragment_container, LoginFragment())
        fragmentTransaction.commit()


        val toggle: ToggleButton = findViewById(R.id.toggleButton)
        toggle.text="New User?\nSign up here!"
        toggle.textOff="New User?\nSign up here!"
        toggle.textOn="Already Registered?\nLogin Now!"
        toggle.setOnCheckedChangeListener { _, isChecked ->
            if(isChecked){

                val fragmentTransaction: FragmentTransaction =
                supportFragmentManager.beginTransaction()
            fragmentTransaction.replace(R.id.fragment_container, SignupFragment())
            fragmentTransaction.commit()
            }
            else{

                val fragmentTransaction: FragmentTransaction =
                    supportFragmentManager.beginTransaction()
                fragmentTransaction.replace(R.id.fragment_container, LoginFragment())
                fragmentTransaction.commit()
            }

        }

//        buttonLogin.setOnClickListener() {
//                val fragmentTransaction: FragmentTransaction =
//                    supportFragmentManager.beginTransaction()
//                fragmentTransaction.replace(R.id.fragment_container, LoginFragment())
//                fragmentTransaction.commit()
//        }
//        buttonSignup.setOnClickListener() {
//                val fragmentTransaction: FragmentTransaction =
//                    supportFragmentManager.beginTransaction()
//                fragmentTransaction.replace(R.id.fragment_container, SignupFragment())
//                fragmentTransaction.commit()
//
//            }
    }
}

