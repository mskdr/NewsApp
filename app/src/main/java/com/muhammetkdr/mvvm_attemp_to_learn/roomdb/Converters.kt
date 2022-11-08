package com.muhammetkdr.mvvm_attemp_to_learn.roomdb

import androidx.room.TypeConverter
import com.muhammetkdr.mvvm_attemp_to_learn.models.Source

class Converters {
    @TypeConverter
    fun fromSource(source: Source?): String? {
        return source?.name
    }

    @TypeConverter
    fun toSource(name: String?): Source {
        return Source(name,name)
    }
}