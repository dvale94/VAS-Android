package com.GCI.attendancesystem.login

import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import br.com.simplepass.loadingbutton.customViews.CircularProgressButton
import com.GCI.attendancesystem.R
import com.GCI.attendancesystem.home.HomeActivity
import com.GCI.attendancesystem.models.Token
import com.GCI.attendancesystem.models.User
import com.GCI.attendancesystem.networking.GCIApiService
import com.GCI.attendancesystem.networking.UserLogin
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : AppCompatActivity() {
    private lateinit var sp: SharedPreferences
    private val apiService = GCIApiService.create()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        sp = getSharedPreferences("login",MODE_PRIVATE)

        val loginButton = findViewById<CircularProgressButton>(R.id.button_login)
        val userName = findViewById<EditText>(R.id.et_username)
        val password = findViewById<EditText>(R.id.et_password)

        val editor = sp.edit()
        if(sp.getBoolean("logged",false)){
            goToMainActivity()
        }

        loginButton.setOnClickListener {
            loginButton.startAnimation()
            apiService.loginUser(UserLogin(userName.text.toString(), password.text.toString()))
                .enqueue(object: Callback<Token> {
                override fun onFailure(call: Call<Token>, t: Throwable) {
                    Toast.makeText(this@LoginActivity,
                        "The username or password you entered is incorrect",
                        Toast.LENGTH_LONG).show()
                }

                override fun onResponse(call: Call<Token>, response: Response<Token>) {
                    editor.putString("gciUser", response.body()?.token).apply()
                    if (response.body()?.token != null) {
                        apiService.authenticateUser("Bearer ${sp.getString("gciUser", "")}")
                            .enqueue(object: Callback<User> {
                            override fun onFailure(call: Call<User>, t: Throwable) {
                                println(t)
                            }

                            override fun onResponse(
                                call: Call<User>,
                                response: Response<User>
                            ) {
                                editor.putBoolean("logged",true).apply()
                                val gson = Gson()
                                val json = gson.toJson(response.body())
                                editor.putString("currentUser", json)
                                editor.apply()
                                goToMainActivity()
                            }

                        })
                    }
                }
            })
        }
    }

    private fun goToMainActivity() {
        val intent = Intent(this@LoginActivity, HomeActivity::class.java)
        startActivity(intent)
        finish()
    }
}
