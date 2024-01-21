package com.example.barcodescanning.data

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject

@HiltViewModel
class OcrViewModel @Inject constructor(
    private val repository: OcrRepository
) : ViewModel(){

    private val _result = MutableSharedFlow<String>()
    val result = _result.asSharedFlow()

    fun uploadImage(file: File){
        viewModelScope.launch {
            repository.uploadImage(file).collect{
                _result.emit(it)
            }
        }
    }
}