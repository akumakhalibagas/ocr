package com.example.barcodescanning.presentation

import android.app.Activity
import android.content.*
import android.graphics.BitmapFactory
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toFile
import com.example.barcodescanning.databinding.ActivityMainBinding
import com.github.dhaval2404.imagepicker.ImagePicker
import com.google.android.gms.vision.Frame
import com.google.android.gms.vision.text.TextRecognizer
import java.io.File

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val startForProfileImageResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
            val resultCode = result.resultCode
            val data = result.data

            when (resultCode) {
                Activity.RESULT_OK -> {
                    val fileUri = data?.data!!
                    vision(fileUri.toFile())
                }
                ImagePicker.RESULT_ERROR -> {
                    Toast.makeText(this, ImagePicker.getError(data), Toast.LENGTH_SHORT).show()
                }
                else -> {
                    Toast.makeText(this, "Task Cancelled", Toast.LENGTH_SHORT).show()
                }
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initListener()
    }

    private fun initListener() {
        binding.apply {

            btnGalery.setOnClickListener {
                ImagePicker.with(this@MainActivity)
                    .compress(1024)
                    .maxResultSize(1080, 1080)
                    .createIntent { intent ->
                        startForProfileImageResult.launch(intent)
                    }
            }

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

    private fun vision(file: File) {
        val textRecognizer: TextRecognizer = TextRecognizer.Builder(applicationContext).build()
        if (file.exists()) {
            val bitmap = BitmapFactory.decodeFile(file.absolutePath)
            val frame = Frame.Builder().setBitmap(bitmap).build()
            val textBlocks = textRecognizer.detect(frame)

            if (textBlocks.size() == 0) {
                Toast.makeText(
                    this,
                    "Tidak Menemukan Text",
                    Toast.LENGTH_SHORT
                ).show()
            }

            for (i in 0 until textBlocks.size()) {
                val textBlock = textBlocks.valueAt(i)
                val text = textBlock.value

                val builder = AlertDialog.Builder(this@MainActivity)
                builder.setTitle("Kino OCR")
                builder.setMessage("--Hasil OCR---\n\n${text}")
                builder.setNegativeButton("Copy") { dialog, which ->
                    val clipboardManager =
                        getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                    val clipData = ClipData.newPlainText("label", text)
                    clipboardManager.setPrimaryClip(clipData)
                    Toast.makeText(this, "Text copied to clipboard!", Toast.LENGTH_SHORT).show()
                }
                builder.setPositiveButton("OK") { dialog, which ->
                    dialog.dismiss()
                }
                builder.create().show()
            }
        }
    }

}