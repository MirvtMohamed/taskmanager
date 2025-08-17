package com.example.taskmanager.data.converters

import androidx.room.TypeConverter

class ListStringConverter {
    @TypeConverter
    fun fromList(list: List<String>?): String? = list?.joinToString(",")

    @TypeConverter
    fun toList(serialized: String?): List<String> =
        if (serialized.isNullOrBlank()) emptyList() else serialized.split(",")
}