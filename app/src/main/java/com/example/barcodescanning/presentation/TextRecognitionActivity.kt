package com.example.barcodescanning.presentation

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.barcodescanning.data.OcrViewModel
import com.example.barcodescanning.databinding.ActivityTextRecognationBinding
import com.google.android.gms.vision.Frame
import com.google.android.gms.vision.text.TextRecognizer
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.latin.TextRecognizerOptions
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.io.File

@AndroidEntryPoint
class TextRecognitionActivity : AppCompatActivity() {

    private var textResult = ""
    private lateinit var binding: ActivityTextRecognationBinding
    private lateinit var sharedPreferences: SharedPreferences
    private val viewModel: OcrViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTextRecognationBinding.inflate(layoutInflater)
        setContentView(binding.root)
        sharedPreferences = getSharedPreferences("my_preferences", Context.MODE_PRIVATE)
        initListener()
        initObserver()
    }

    private fun initListener() {
        val ocrimage = sharedPreferences.getString("ocrimage", "")
        binding.apply {

            btnMlkit.setOnClickListener { mlkit() }
            btnVision.setOnClickListener { vision() }
            btnUpload.setOnClickListener {
                viewModel.uploadImage(
                    File(
                        externalMediaDirs[0],
                        ocrimage.toString()
                    )
                )
            }

        }
    }

    private fun initObserver() {
        lifecycleScope.launch {
            viewModel.result.collect {
                Toast.makeText(this@TextRecognitionActivity, "Upload Success", Toast.LENGTH_SHORT).show()
                Log.e("xxx", it)
            }
        }
    }

    private fun vision() {
        val ocrimage = sharedPreferences.getString("ocrimage", "")
        val textRecognizer: TextRecognizer = TextRecognizer.Builder(applicationContext).build()
        val file = File(externalMediaDirs[0], ocrimage.toString())
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

                val customPattern = sharedPreferences.getString("pattern", "")
                val regex = Regex(customPattern.toString())
                var matchFound = false

                for (match in regex.findAll(text)) {
                    matchFound = true
                    toWa("---Hasil Pattern---\n\n${match.value}\n\n\n---Hasil OCR---\n\n${text}")
                }

                if (!matchFound) {
                    toWa("---Hasil OCR---\n\n$text")
                }
            }
        }
    }

    private fun mlkit() {
        val ocrimage = sharedPreferences.getString("ocrimage", "")
        val textRecognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS)
        val inputImage =
            InputImage.fromFilePath(this, Uri.fromFile(File(externalMediaDirs[0],
                ocrimage.toString()
            )))
        val result = textRecognizer.process(inputImage)
        result.addOnSuccessListener { text ->

            if (text.textBlocks.size == 0) {
                Toast.makeText(
                    this,
                    "Tidak Menemukan Text",
                    Toast.LENGTH_SHORT
                ).show()
            }

            for (textBlock in text.textBlocks) {
                val blockText = textBlock.text
                textResult += blockText
            }

            val customPattern = sharedPreferences.getString("pattern", "")
            val regex = Regex(customPattern.toString())
            var matchFound = false

            for (match in regex.findAll(textResult)) {
                matchFound = true
                toWa("---Hasil Pattern---\n\n${match.value}\n\n\n---Hasil OCR---\n\n${textResult}")
            }

            if (!matchFound) {
                toWa("---Hasil OCR---\n\n$textResult")
            }
        }
    }

    private fun toWa(data: String) {
        val intent = Intent()
        intent.action = Intent.ACTION_SEND
        intent.putExtra(Intent.EXTRA_TEXT, data)
        intent.type = "text/plain"
        intent.setPackage("com.whatsapp")

        try {
            startActivity(intent)
        } catch (e: ActivityNotFoundException) {
            Toast.makeText(
                this,
                "WhatsApp tidak terpasang di perangkat Anda",
                Toast.LENGTH_SHORT
            ).show()
        }
    }
}