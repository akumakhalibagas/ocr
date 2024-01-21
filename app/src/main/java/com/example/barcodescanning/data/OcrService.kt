package com.example.barcodescanning.data

import okhttp3.MultipartBody
import retrofit2.http.*

interface OcrService {

    @Multipart
    @POST("upload.php")
    suspend fun uploadImage(
        @Part file: MultipartBody.Part,
    ): String

}