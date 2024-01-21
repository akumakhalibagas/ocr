package com.example.barcodescanning.presentation

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.barcodescanning.databinding.ActivityPatternBinding

class PatternActivity : AppCompatActivity() {

    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var binding: ActivityPatternBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPatternBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sharedPreferences = getSharedPreferences("my_preferences", Context.MODE_PRIVATE)
        val pattern = sharedPreferences.getString("pattern", "")
        binding.etPatterShow.setText(pattern)

        binding.btnSave.setOnClickListener {
            val customPattern = binding.etPatterEdit.text.toString()
            sharedPreferences.edit().apply { putString("pattern", customPattern) }.apply()
            Toast.makeText(this, "saved", Toast.LENGTH_SHORT).show()
            onBackPressed()
        }
    }
}