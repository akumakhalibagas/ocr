package com.example.barcodescanning.presentation

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import com.example.barcodescanning.databinding.ActivityPhotoBinding
import com.example.barcodescanning.getTimestamp
import com.google.common.util.concurrent.ListenableFuture
import java.io.File
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class PhotoActivity : AppCompatActivity() {

    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var binding: ActivityPhotoBinding
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
        binding = ActivityPhotoBinding.inflate(layoutInflater)
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
                .setTargetAspectRatio(AspectRatio.RATIO_16_9)
                .setJpegQuality(100)
                .build()
            imagePreview = Preview.Builder()
                .setTargetAspectRatio(AspectRatio.RATIO_16_9)
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
                        startActivity(
                            Intent(
                                this@PhotoActivity,
                                TextRecognitionActivity::class.java
                            )
                        )
                    }
                }

                override fun onError(exception: ImageCaptureException) {
                    runOnUiThread {
                        Toast.makeText(this@PhotoActivity, "Error taking photo", Toast.LENGTH_SHORT)
                            .show()
                    }
                }
            })
    }
}