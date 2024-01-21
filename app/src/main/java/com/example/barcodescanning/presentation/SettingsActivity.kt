package com.example.barcodescanning.presentation

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.barcodescanning.databinding.ActivitySettingsBinding

class SettingsActivity : AppCompatActivity() {

    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var binding: ActivitySettingsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sharedPreferences = getSharedPreferences("my_preferences", Context.MODE_PRIVATE)
        initView()
        initListener()
    }

    private fun initView(){
        val sales = sharedPreferences.getString("sales", "kino")
        val server = sharedPreferences.getString("server", "")
        val port = sharedPreferences.getString("port", "")
        val username = sharedPreferences.getString("username", "")
        val password = sharedPreferences.getString("password", "")

        binding.apply {
            etSales.setText(sales)
//            etServer.setText(server)
//            etPort.setText(port)
//            etUsername.setText(username)
//            etPassword.setText(password)
        }
    }

    private fun initListener(){
        binding.apply {
            btnSave.setOnClickListener {
                val sales = etSales.text ?: "kino"
//                val server = etServer.text.toString()
//                val port = etPort.text.toString()
//                val username = etUsername.text.toString()
//                val password = etPassword.text.toString()
                sharedPreferences.edit().apply {
                    putString("sales", sales.toString())
//                    putString("server", server)
//                    putString("port", port)
//                    putString("username", username)
//                    putString("password", password)
                }.apply()
                Toast.makeText(this@SettingsActivity, "saved", Toast.LENGTH_SHORT).show()
                onBackPressed()
            }
        }
    }


}