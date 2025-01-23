package com.firmino.hinedigital.model.repository

import androidx.annotation.WorkerThread
import com.firmino.hinedigital.model.entity.Evaluation
import com.firmino.hinedigital.model.dao.EvaluationDao
import kotlinx.coroutines.flow.Flow

class EvaluationRepository(private val evaluationDao: EvaluationDao) {
    val allEvaluations: Flow<List<Evaluation>> = evaluationDao.getAll()

    @WorkerThread
    suspend fun insert(evaluation: Evaluation): Long {
        return evaluationDao.insert(evaluation)
    }

    @WorkerThread
    suspend fun delete(id: Long) {
        evaluationDao.delete(id)
    }

    @WorkerThread
    suspend fun update(evaluation: Evaluation) {
        evaluationDao.update(evaluation)
    }
}