package com.example.barcodescanning.data

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class OcrDataSource @Inject constructor(
    private val ocrService: OcrService
){
    fun uploadImage(image: File): Flow<String> =
        flow {
            try {
                val requestBody = MultipartBody.Part.createFormData(
                    "file",
                    image.name,
                    RequestBody.create("image/jpeg".toMediaTypeOrNull(), image)
                )
                val response = ocrService.uploadImage(requestBody)
                emit(response)
            } catch (e: Exception) {
                emit(e.message.toString())
            }
        }.flowOn(Dispatchers.IO)
}