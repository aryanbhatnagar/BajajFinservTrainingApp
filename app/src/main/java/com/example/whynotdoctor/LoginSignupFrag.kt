package com.example.whynotdoctor

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button

import androidx.appcompat.app.AppCompatActivity
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.example.whynotdoctor.admin.HomeAdmin
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST


data class User(
    var _id : String,
    var name : String,
    var email : String,
    var password: String,
    var cnf_password: String,
    var isAdmin: Boolean,
    var creation_date : String,
    var __v: Number
)
data class PostUser(
    var name : String,
    var email : String,
    var password: String,
    var cnf_password: String,
    var isAdmin: Boolean,

)

interface UserAPI {
    @GET("user")
    fun getdata() : Call<List<User>>
}

interface PostUserAPI {
    @POST("user")
    fun postdata(@Body user:PostUser) : Call<Void>
}


class LoginFragment : Fragment() {
    private var mUsernameEditText: TextInputEditText? = null
    private var mPasswordEditText: TextInputEditText? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view: View = inflater.inflate(R.layout.login_frag, container, false)
        mUsernameEditText = view.findViewById(R.id.login_username)
        mPasswordEditText = view.findViewById(R.id.login_password)
        val loginButton: Button = view.findViewById(R.id.button_login)
        loginButton.setOnClickListener{


                val username = mUsernameEditText!!.text.toString()
                val password = mPasswordEditText!!.text.toString()


            val retrofit = Retrofit.Builder()
                .baseUrl(GlobalData.getInstance().URL_G.toString())
                .addConverterFactory(GsonConverterFactory.create())
                .build()

            val api = retrofit.create(UserAPI::class.java)

            val call: Call<List<User>> = api.getdata()
            call.enqueue(object : Callback<List<User>> {

                override fun onResponse(call: Call<List<User>>, response: Response<List<User>>) {

                    val dataList = response.body()
                    Log.d("DATA",dataList.toString())
                    if (dataList != null) {
                        var c=0
                        for(userlist in dataList){
                            if(c.equals(0))
                            if(userlist.email.equals(username)){
                                if(userlist.password.equals(password)){
                                    if(userlist.isAdmin==true){
                                        requireActivity().run{
                                            startActivity(Intent(this, HomeAdmin::class.java))
                                            finish()
                                        }

                                    }
                                    else{
                                        c=1
                                        requireActivity().run{
                                            startActivity(Intent(this, MainActivity::class.java))
                                            finish()
                                        }
                                        c=0

                                    }
                                }
                                else{
                                    Toast.makeText(getContext(), "Wrong Password", Toast.LENGTH_SHORT).show()
                                    c=0
                                }
                            }

                        }
                    }

                }

                override fun onFailure(call: Call<List<User>>, t: Throwable) {

                    Toast.makeText(getContext(), "Network Issue", Toast.LENGTH_SHORT).show()
                }
            })


            }
        return view
    }
}

class SignupFragment : Fragment() {
    private var mUsername: TextInputEditText?  = null
    private var mEmail: TextInputEditText?  = null
    private var mPassword: TextInputEditText?  = null
    private var cnfPassword: TextInputEditText?  = null

    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view: View = inflater.inflate(R.layout.signup_frag, container, false)
        mUsername = view.findViewById(R.id.signup_username)
        mEmail = view.findViewById(R.id.signup_email)
        mPassword = view.findViewById(R.id.signup_password)
        cnfPassword = view.findViewById(R.id.cnf_password)
        val signupButton: Button = view.findViewById(R.id.button_signup)
        signupButton.setOnClickListener {

                val username = mUsername!!.text.toString()
                val email = mEmail!!.text.toString()
                val password = mPassword!!.text.toString()
                val cnf_password =cnfPassword!!.text.toString()


            if(cnf_password.equals(password)){
                val postData_class : PostUser = PostUser(username,email,password,cnf_password,false)

                val retrofit = Retrofit.Builder()
                    .baseUrl(GlobalData.getInstance().URL_G.toString())
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()

                val api = retrofit.create(PostUserAPI::class.java)
                val call: Call<Void> = api.postdata(postData_class)
                call.enqueue(object : Callback<Void?> {
                    override fun onResponse(call: Call<Void?>, response: Response<Void?>) {
                        Toast.makeText(getContext(), "Signup Successful", Toast.LENGTH_SHORT).show()
                    }

                    override fun onFailure(call: Call<Void?>, t: Throwable) {
                        Toast.makeText(getContext(), "Could Not create user", Toast.LENGTH_SHORT).show()
                    }
                })




            }
            else{
                Toast.makeText(getContext(), "Confirm Password should be same as Password", Toast.LENGTH_SHORT).show()
            }

        }



        return view
    }
}
