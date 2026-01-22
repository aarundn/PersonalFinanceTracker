package com.example.core.utils

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


fun parseDateString(dateString: Long): String {
    val dateFormatter = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault())
    return dateFormatter.format(Date(dateString))

}
