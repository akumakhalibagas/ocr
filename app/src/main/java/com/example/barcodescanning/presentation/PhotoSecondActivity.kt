package com.example.barcodescanning.presentation

import android.content.Context
import android.content.SharedPreferences
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import com.example.barcodescanning.databinding.ActivityPhotoSecondBinding
import com.example.barcodescanning.getTimestamp
import com.google.android.gms.vision.Frame
import com.google.android.gms.vision.text.TextRecognizer
import com.google.common.util.concurrent.ListenableFuture
import java.io.File
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class PhotoSecondActivity : AppCompatActivity() {

    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var binding: ActivityPhotoSecondBinding
    private lateinit var cameraProviderFuture: ListenableFuture<ProcessCameraProvider>
    private lateinit var cameraSelector: CameraSelector
    private var imageCapture: ImageCapture? = null
    private var imagePreview: Preview? = null
    private lateinit var imgCaptureExecutor: ExecutorService
    private val cameraPermissionResult =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { permissionGranted ->
            if (permissionGranted) {
                startCamera()
            } else {
                Toast.makeText(this, "The camera permission is necessary", Toast.LENGTH_SHORT)
                    .show()
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPhotoSecondBinding.inflate(layoutInflater)
        setContentView(binding.root)
        cameraProviderFuture = ProcessCameraProvider.getInstance(this)
        cameraSelector = CameraSelector.DEFAULT_FRONT_CAMERA
        imgCaptureExecutor = Executors.newSingleThreadExecutor()
        cameraPermissionResult.launch(android.Manifest.permission.CAMERA)
        sharedPreferences = getSharedPreferences("my_preferences", Context.MODE_PRIVATE)
        initListener()
    }

    private fun initListener() {
        binding.apply {
            btScan.setOnClickListener { saveBarcode() }
        }
    }

    private fun startCamera() {
        cameraProviderFuture.addListener({
            val cameraProvider = cameraProviderFuture.get()
            imageCapture = ImageCapture.Builder()
                .setTargetAspectRatio(AspectRatio.RATIO_DEFAULT)
                .setJpegQuality(30)
                .build()
            imagePreview = Preview.Builder()
                .setTargetAspectRatio(AspectRatio.RATIO_DEFAULT)
                .build()

            try {
                imagePreview?.setSurfaceProvider(binding.preview.surfaceProvider)
                cameraProvider.unbindAll()
                cameraProvider.bindToLifecycle(this, cameraSelector, imagePreview, imageCapture)
            } catch (e: Exception) {
                Toast.makeText(this, "Use case binding failed", Toast.LENGTH_SHORT).show()
            }
        }, ContextCompat.getMainExecutor(this))
    }

    private fun saveBarcode() {
        val sales = sharedPreferences.getString("sales", "kino")
        val fileName = "${sales}_IMG_${getTimestamp()}.jpg"
        sharedPreferences.edit().apply { putString("ocrimage", fileName) }.apply()
        val fileImage = File(externalMediaDirs[0], fileName)
        imageCapture!!.takePicture(
            ImageCapture.OutputFileOptions.Builder(fileImage).build(),
            imgCaptureExecutor,
            object : ImageCapture.OnImageSavedCallback {
                override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
                    runOnUiThread {
                        vision()
                    }
                }

                override fun onError(exception: ImageCaptureException) {
                    runOnUiThread {
                        Toast.makeText(this@PhotoSecondActivity, "Error taking photo", Toast.LENGTH_SHORT)
                            .show()
                    }
                }
            })
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

                val builder = AlertDialog.Builder(this@PhotoSecondActivity)
                builder.setTitle("Kino OCR")
                builder.setMessage("--Hasil OCR---\n\n${text}")
                builder.setPositiveButton("OK") { dialog, which ->
                    dialog.dismiss()
                }
                builder.create().show()

//                for (match in regex.findAll(text)) {
//                    matchFound = true
//                    val builder = AlertDialog.Builder(this@PhotoSecondActivity)
//                    builder.setTitle("Your Title")
//                    builder.setMessage("---Hasil Pattern---\n\n${match.value}\n\n\n---Hasil OCR---\n\n${text}")
//                    builder.create().show()
//                }
//
//                if (!matchFound) {
//                    Toast.makeText(this, "---Hasil OCR---\n\n$text", Toast.LENGTH_SHORT).show()
//                }
            }
        }
    }
}