package com.example.core.utils

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


fun parseDateString(dateString: Long, isDate : Boolean = true): String {
    val dateFormatter = if (isDate)SimpleDateFormat("MMM dd, yyyy", Locale.getDefault())
    else SimpleDateFormat("hh:mm a", Locale.getDefault())
    return dateFormatter.format(Date(dateString))

}
