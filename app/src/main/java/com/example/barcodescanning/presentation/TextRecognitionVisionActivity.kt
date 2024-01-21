package com.example.barcodescanning.presentation

import android.content.ActivityNotFoundException
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.barcodescanning.databinding.ActivityTextRecognitionVisionBinding
import com.google.android.gms.vision.Frame
import com.google.android.gms.vision.text.TextRecognizer
import java.io.File

class TextRecognitionVisionActivity : AppCompatActivity() {

    private lateinit var binding: ActivityTextRecognitionVisionBinding
    private lateinit var textRecognizer: TextRecognizer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTextRecognitionVisionBinding.inflate(layoutInflater)
        setContentView(binding.root)
        textRecognizer = TextRecognizer.Builder(applicationContext).build()

//        val file = File(externalMediaDirs[0], "makhalibagas.jpg")
//        if (file.exists()) {
//            val bitmap = BitmapFactory.decodeFile(file.absolutePath)
//            processImage(bitmap)
//        }
    }

//    private fun processImage(bitmap: Bitmap) {
//        val frame = Frame.Builder().setBitmap(bitmap).build()
//        val textBlocks = textRecognizer.detect(frame)
//
//        for (i in 0 until textBlocks.size()) {
//            val textBlock = textBlocks.valueAt(i)
//            val text = textBlock.value
//
//            // Process or display the recognized text as needed
//            Toast.makeText(this, text, Toast.LENGTH_SHORT).show()
//            toWa("---Hasil OCR---\n\n$text")
//            Log.d("OCR", "Recognized text: $text")
//        }
//    }
//
//    private fun toWa(data: String) {
//        val intent = Intent()
//        intent.action = Intent.ACTION_SEND
//        intent.putExtra(Intent.EXTRA_TEXT, data)
//        intent.type = "text/plain"
//        intent.setPackage("com.whatsapp")
//
//        try {
//            startActivity(intent)
//        } catch (e: ActivityNotFoundException) {
//            Toast.makeText(
//                this,
//                "WhatsApp tidak terpasang di perangkat Anda",
//                Toast.LENGTH_SHORT
//            ).show()
//        }
//    }

//    private fun uploadImageToFTP(imageFile: File) {
//
//        val server = sharedPreferences.getString("server", "")
//        val port = sharedPreferences.getString("port", "")
//        val username = sharedPreferences.getString("username", "")
//        val password = sharedPreferences.getString("password", "")
//
//        val ftpClient = FTPClient()
//
//        try {
//            ftpClient.connect("ftp:http://labs.alldataint.com/upload.php") // Replace with your FTP server address
//            //ftpClient.login("", "") // Replace with your FTP credentials
//
//            // Set the file transfer mode to binary
//            ftpClient.setFileType(FTP.BINARY_FILE_TYPE)
//
//            val inputStream = BufferedInputStream(FileInputStream(imageFile))
//            val remoteFileName = imageFile.name
//
//            // Upload the file to the FTP server
//            ftpClient.storeFile(remoteFileName, inputStream)
//
//            inputStream.close()
//            ftpClient.logout()
//            ftpClient.disconnect()
//
//            // File uploaded successfully
//            // You can perform any necessary actions here after the upload is complete
//            Toast.makeText(this, "complete", Toast.LENGTH_SHORT).show()
//        } catch (e: Exception) {
//            e.printStackTrace()
//            Toast.makeText(this, e.message.toString(), Toast.LENGTH_SHORT).show()
//            // Handle any exceptions that occurred during the upload process
//        }
//    }
}