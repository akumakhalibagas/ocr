package com.example.barcodescanning

import java.text.SimpleDateFormat
import java.util.*

fun getTimestamp(): String {
    val timestamp = System.currentTimeMillis()
    val dateFormat = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault())
    val date = Date(timestamp)
    return dateFormat.format(date)
}