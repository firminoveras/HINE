package com.firmino.hinedigital.model.converters

import androidx.room.ProvidedTypeConverter
import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

@ProvidedTypeConverter
class ConverterIntArray {
    @TypeConverter
    fun fromString(value: String): MutableList<MutableList<Int>>{
        val listType = object: TypeToken<MutableList<MutableList<Int>>>(){}.type
        return Gson().fromJson(value,listType)
    }

    @TypeConverter
    fun fromList(list: MutableList<MutableList<Int>>) : String{
        return Gson().toJson(list)
    }
}

@ProvidedTypeConverter
class ConverterStringArray {
    @TypeConverter
    fun fromString(value: String): MutableList<MutableList<String>>{
        val listType = object:TypeToken<MutableList<MutableList<String>>>(){}.type
        return Gson().fromJson(value,listType)
    }

    @TypeConverter
    fun fromList(list: MutableList<MutableList<String>>) : String{
        return Gson().toJson(list)
    }
}
