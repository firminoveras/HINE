package com.firmino.hinedigital

import android.app.Application
import com.firmino.hinedigital.model.EvaluationDatabase
import com.firmino.hinedigital.model.repository.EvaluationRepository

class HINEApplication : Application(){
    private val database by lazy { EvaluationDatabase.getInstance(this)}
    val repository by lazy { EvaluationRepository(database.evaluationDao())}
}