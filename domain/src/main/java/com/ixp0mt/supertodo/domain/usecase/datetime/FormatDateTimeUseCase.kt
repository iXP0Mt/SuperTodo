package com.ixp0mt.supertodo.domain.usecase.datetime

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class FormatDateTimeUseCase {
    fun formatDate(millis: Long?): String? {
        if(millis == null) return null

        return SimpleDateFormat("dd.MM.yyyy", Locale.getDefault()).format(Date(millis))
    }

    fun formatTime(millis: Long?): String? {
        if(millis == null) return null

        return SimpleDateFormat("HH:mm", Locale.getDefault()).format(Date(millis))
    }
}