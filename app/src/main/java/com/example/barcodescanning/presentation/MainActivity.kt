package com.example.barcodescanning.presentation

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.barcodescanning.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initListener()
    }

    private fun initListener() {
        binding.apply {
            btnPhase2.setOnClickListener {
                startActivity(Intent(this@MainActivity, PhotoSecondActivity::class.java))
            }

            btnPhoto.setOnClickListener {
                startActivity(Intent(this@MainActivity, PhotoActivity::class.java))
            }

            btnPattern.setOnClickListener {
                startActivity(Intent(this@MainActivity, PatternActivity::class.java))
            }

            btnDictionary.setOnClickListener {
                Toast.makeText(this@MainActivity, "feature in Progress", Toast.LENGTH_SHORT).show()
            }

            btnSettings.setOnClickListener {
                startActivity(Intent(this@MainActivity, SettingsActivity::class.java))
            }
        }
    }

}