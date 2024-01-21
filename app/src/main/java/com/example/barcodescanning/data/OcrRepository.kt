package com.example.barcodescanning.data

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import java.io.File
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class OcrRepository @Inject constructor(private val ocrDataSource: OcrDataSource) {

    fun uploadImage(image: File): Flow<String> = flow {
        val response = ocrDataSource.uploadImage(image).first()
        emit(response)
    }

}