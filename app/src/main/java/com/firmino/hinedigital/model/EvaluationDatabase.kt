package com.firmino.hinedigital.model

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.firmino.hinedigital.model.converters.ConverterIntArray
import com.firmino.hinedigital.model.converters.ConverterStringArray
import com.firmino.hinedigital.model.entity.Evaluation
import com.firmino.hinedigital.model.dao.EvaluationDao

@Database(entities = [Evaluation::class], version = 10)
abstract class EvaluationDatabase : RoomDatabase() {
    abstract fun evaluationDao(): EvaluationDao

    companion object {
        @Volatile
        private var INSTANCE: EvaluationDatabase? = null

        fun getInstance(context: Context): EvaluationDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room
                    .databaseBuilder(context.applicationContext, EvaluationDatabase::class.java, "evaluation_database")
                    .fallbackToDestructiveMigration()
                    .addTypeConverter(ConverterIntArray())
                    .addTypeConverter(ConverterStringArray())
                    .build()
                INSTANCE = instance
                instance
            }
        }

    }
}