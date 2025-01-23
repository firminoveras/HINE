package com.firmino.hinedigital.model.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.firmino.hinedigital.model.converters.ConverterIntArray
import com.firmino.hinedigital.model.converters.ConverterStringArray

@Entity(tableName = "evaluation_table")
@TypeConverters(ConverterIntArray::class, ConverterStringArray::class)
data class Evaluation(
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "id") val id: Long = 0L,
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "gender") val gender: String,
    @ColumnInfo(name = "birthday") val birthday: String,
    @ColumnInfo(name = "gestational_weeks") val gestationalWeeks: Int,
    @ColumnInfo(name = "corrected_age") val correctedAge: Int,
    @ColumnInfo(name = "cephalicSize") val cephalicSize: Int,
    @ColumnInfo(name = "scores") var scores: MutableList<MutableList<Int>> = mutableListOf(
        mutableListOf(0, 0, 0, 0, 0),
        mutableListOf(0, 0, 0, 0, 0, 0),
        mutableListOf(0, 0),
        mutableListOf(0, 0, 0, 0, 0, 0, 0, 0),
        mutableListOf(0, 0, 0, 0, 0),
        mutableListOf(0, 0, 0, 0, 0, 0, 0, 0),
        mutableListOf(0, 0, 0),
    ),
    @ColumnInfo(name = "comments") var comments: MutableList<MutableList<String>> = mutableListOf(
        mutableListOf("", "", "", "", ""),
        mutableListOf("", "", "", "", "", ""),
        mutableListOf("", ""),
        mutableListOf("", "", "", "", "", "", "", ""),
        mutableListOf("", "", "", "", ""),
        mutableListOf("", "", "", "", "", "", "", ""),
        mutableListOf("", "", ""),
    ),
    @ColumnInfo(name = "asymmetries") var asymmetries:MutableList<MutableList<Int>> = mutableListOf(
        mutableListOf(0, 0, 0, 0, 0),
        mutableListOf(0, 0, 0, 0, 0, 0),
        mutableListOf(0, 0),
        mutableListOf(0, 0, 0, 0, 0, 0, 0, 0),
        mutableListOf(0, 0, 0, 0, 0),
        mutableListOf(0, 0, 0, 0, 0, 0, 0, 0),
        mutableListOf(0, 0, 0),
    ),
    @ColumnInfo(name = "asymmetries_comments") var asymmetriesComments:MutableList<MutableList<String>> = mutableListOf(
        mutableListOf("", "", "", "", ""),
        mutableListOf("", "", "", "", "", ""),
        mutableListOf("", ""),
        mutableListOf("", "", "", "", "", "", "", ""),
        mutableListOf("", "", "", "", ""),
        mutableListOf("", "", "", "", "", "", "", ""),
        mutableListOf("", "", ""),
    ),
    ) {

    fun getGlobalScore(): Int {
        var count = 0
        scores.forEachIndexed{ index, ints -> if(index < 5) count += ints.sum() }
        return count
    }

    fun getComportamentalScore(): Int {
        var count = 0
        scores.forEachIndexed{ index, ints -> if(index >= 5) count += ints.sum() }
        return count
    }

    fun getAssymetriesCount(): Int {
        var count = 0
        asymmetries.forEach { count += it.sum() }
        return count
    }

    fun getScoreByEvaluation(evaluationNumber: Int): Int {
        return scores[evaluationNumber].sum()
    }

    fun getMaxScoreByEvaluation(evaluationNumber: Int): Int {
        return when (evaluationNumber) {
            0 -> 15
            1 -> 18
            2 -> 6
            3 -> 24
            4 -> 15
            5 -> 32
            6 -> 15
            else -> 0
        }
    }

    companion object {
        const val GLOBAL_SCORE_MAX = 78
        const val COMPORTAMENTAL_SCORE_MAX = 47
        const val ASSYMETRY_MAX = 37
    }
}