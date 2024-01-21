package com.example.barcodescanning.presentation

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.barcodescanning.databinding.ActivityBarcodeScanningBinding
import com.google.mlkit.vision.barcode.BarcodeScannerOptions
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.barcode.common.Barcode
import com.google.mlkit.vision.common.InputImage
import java.io.File

class BarcodeScanningActivity : AppCompatActivity() {

    private lateinit var binding: ActivityBarcodeScanningBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBarcodeScanningBinding.inflate(layoutInflater)
        setContentView(binding.root)
        scanBarcodes(
            InputImage.fromFilePath(
                this,
                Uri.fromFile(File(externalMediaDirs[0], "makhalibagas.jpg"))
            )
        )


    }


    private fun scanBarcodes(image: InputImage) {
        val options = BarcodeScannerOptions.Builder()
            .setBarcodeFormats(Barcode.FORMAT_ALL_FORMATS)
            .build()

        val scanner = BarcodeScanning.getClient(options)
        val result = scanner.process(image)
        result.addOnSuccessListener { barcodes ->
            for (barcode in barcodes) {
                binding.tvResultBarcode.text = barcode.displayValue
                binding.imgWa.setOnClickListener {
                    val sendIntent = Intent()
                    sendIntent.action = Intent.ACTION_SEND
                    sendIntent.putExtra(Intent.EXTRA_TEXT, barcode.displayValue)
                    sendIntent.type = "text/plain"
                    sendIntent.setPackage("com.whatsapp")

                    try {
                        startActivity(sendIntent)
                    } catch (e: ActivityNotFoundException) {
                        Toast.makeText(
                            this,
                            "WhatsApp tidak terpasang di perangkat Anda",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        }
        result.addOnFailureListener {
            showError(it.message.toString())
        }
    }

    private fun showError(it: String) {
        Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
    }
}