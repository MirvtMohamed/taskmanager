package com.example.taskmanager.data

import androidx.room.TypeConverter
import java.util.*

class ListStringConverter {
    @TypeConverter
    fun fromList(list: List<String>?): String? = list?.joinToString(",")

    @TypeConverter
    fun toList(serialized: String?): List<String> =
        if (serialized.isNullOrBlank()) emptyList() else serialized.split(",")
}